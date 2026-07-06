package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.domain.AcmeAccount
import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.usecase.acme.repository.AcmeAccountRepository
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import fr.gouv.agora.usecase.acme.repository.CloudflareCertificateDeployer
import org.shredzone.acme4j.AccountBuilder
import org.shredzone.acme4j.Session
import org.shredzone.acme4j.challenge.Http01Challenge
import org.shredzone.acme4j.util.CSRBuilder
import org.shredzone.acme4j.util.KeyPairUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.StringReader
import java.io.StringWriter
import java.security.KeyPair
import java.security.cert.X509Certificate
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Base64

@Service
class AcmeCertificateRenewalUseCase(
    private val acmeConfig: AcmeConfig,
    private val certificateRepository: AcmeCertificateRepository,
    private val accountRepository: AcmeAccountRepository,
    private val challengeStore: AcmeChallengeStore,
    private val cloudflareDeployer: CloudflareCertificateDeployer,
    private val clock: Clock,
) {

    private val logger = LoggerFactory.getLogger(AcmeCertificateRenewalUseCase::class.java)

    companion object {
        private const val RENEWAL_THRESHOLD_DAYS = 30L
        private const val POLLING_MAX_ATTEMPTS = 10
        private const val POLLING_INTERVAL_MS = 3_000L
    }

    fun renewIfNeeded() {
        if (!acmeConfig.enabled) {
            logger.info("ACME renewal is disabled (ACME_ENABLED=false). Skipping.")
            return
        }

        val domain = acmeConfig.domain
        val serverUrl = acmeConfig.serverUrl
        val now = LocalDateTime.now(clock)

        // 1. Vérification expiration
        val existingCert = certificateRepository.loadCertificate(domain)
        if (existingCert != null && existingCert.expiresAt.isAfter(now.plusDays(RENEWAL_THRESHOLD_DAYS))) {
            logger.info("Certificate for $domain is still valid until ${existingCert.expiresAt}. No renewal needed.")
            return
        }

        if (existingCert == null) {
            logger.info("No existing certificate found for $domain. Proceeding with first issuance.")
        } else {
            logger.info("Certificate for $domain expires at ${existingCert.expiresAt}. Renewal needed.")
        }

        // 2. Chargement/création keypair compte
        val storedAccount = accountRepository.loadAccount(serverUrl)
        val accountKeyPair: KeyPair = if (storedAccount != null) {
            KeyPairUtils.readKeyPair(StringReader(storedAccount.keyPem))
        } else {
            logger.info("No ACME account found for $serverUrl. Generating new key pair.")
            KeyPairUtils.createKeyPair(2048)
        }

        // 3. Session acme4j
        val session = Session(serverUrl)

        // 4. Compte ACME
        val accountBuilder = AccountBuilder().useKeyPair(accountKeyPair)
        val account = if (storedAccount?.accountUrl != null) {
            logger.info("Using existing ACME account: ${storedAccount.accountUrl}")
            accountBuilder.onlyExisting().create(session)
        } else {
            logger.info("Creating new ACME account with EAB credentials.")
            accountBuilder
                .withKeyIdentifier(acmeConfig.eabKid, acmeConfig.eabHmacKey)
                .agreeToTermsOfService()
                .create(session)
        }

        // 5. Order
        val order = account.newOrder().domain(domain).create()

        // 6. Challenge HTTP-01
        val authorization = order.authorizations.first()
        val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
            ?: throw IllegalStateException("No HTTP-01 challenge available for domain $domain")
        challengeStore.storeChallenge(challenge.token, challenge.authorization)

        try {
            // 7. Trigger validation
            challenge.trigger()

            // 8. Polling jusqu'à VALID
            repeat(POLLING_MAX_ATTEMPTS) { attempt ->
                Thread.sleep(POLLING_INTERVAL_MS)
                challenge.update()
                when (challenge.status) {
                    org.shredzone.acme4j.Status.VALID -> return@repeat
                    org.shredzone.acme4j.Status.INVALID -> {
                        throw AcmeChallengeFailedException("ACME HTTP-01 challenge INVALID for domain $domain")
                    }
                    else -> logger.info("Challenge status: ${challenge.status} (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
                }
                if (attempt == POLLING_MAX_ATTEMPTS - 1) {
                    throw AcmeChallengeTimeoutException("ACME HTTP-01 challenge timed out after $POLLING_MAX_ATTEMPTS attempts for domain $domain")
                }
            }
        } finally {
            challengeStore.clearChallenge(challenge.token)
        }

        // 9. Keypair domaine + CSR
        val domainKeyPair = KeyPairUtils.createKeyPair(2048)
        val csrBuilder = CSRBuilder()
        csrBuilder.addDomain(domain)
        csrBuilder.sign(domainKeyPair)

        // 10. Finalisation Order
        order.execute(csrBuilder.encoded)

        repeat(POLLING_MAX_ATTEMPTS) { attempt ->
            Thread.sleep(POLLING_INTERVAL_MS)
            order.update()
            when (order.status) {
                org.shredzone.acme4j.Status.VALID -> return@repeat
                org.shredzone.acme4j.Status.INVALID -> throw AcmeChallengeFailedException("ACME order became INVALID for domain $domain")
                else -> logger.info("Order status: ${order.status} (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
            }
        }

        // 11. Téléchargement certificat
        val certificate = order.certificate ?: throw IllegalStateException("ACME order completed but certificate is null for domain $domain")
        val certChain = certificate.certificateChain

        // 12. PEM chain (encodage Base64 standard, sans dépendance à AcmeUtils)
        val certPem = buildString {
            certChain.forEach { cert ->
                append("-----BEGIN CERTIFICATE-----\n")
                append(Base64.getMimeEncoder(64, "\n".toByteArray()).encodeToString(cert.encoded))
                append("\n-----END CERTIFICATE-----\n")
            }
        }

        // 13. Extraction date d'expiration depuis le premier certificat de la chaîne
        val x509Cert = certChain.first() as X509Certificate
        val expiresAt = x509Cert.notAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        // 14. PEM clé privée domaine
        val domainPrivKeyWriter = StringWriter()
        KeyPairUtils.writeKeyPair(domainKeyPair, domainPrivKeyWriter)
        val domainPrivKeyPem = domainPrivKeyWriter.toString()

        // 15. Persistance certificat
        certificateRepository.saveCertificate(
            AcmeCertificate(
                domain = domain,
                certificatePem = certPem,
                privateKeyPem = domainPrivKeyPem,
                expiresAt = expiresAt,
            )
        )

        // 16. Déploiement Cloudflare
        cloudflareDeployer.deployCertificate(certPem, domainPrivKeyPem)

        // 17. Persistance compte ACME
        val accountKeyWriter = StringWriter()
        KeyPairUtils.writeKeyPair(accountKeyPair, accountKeyWriter)
        accountRepository.saveAccount(
            AcmeAccount(
                serverUrl = serverUrl,
                accountUrl = account.location?.toString(),
                keyPem = accountKeyWriter.toString(),
            )
        )

        logger.info("ACME certificate renewal completed successfully for domain $domain. Expires at $expiresAt.")
    }
}

class AcmeChallengeFailedException(message: String) : RuntimeException(message)
class AcmeChallengeTimeoutException(message: String) : RuntimeException(message)
