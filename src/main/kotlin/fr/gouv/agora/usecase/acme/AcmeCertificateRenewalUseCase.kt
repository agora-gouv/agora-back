package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.domain.AcmeAccount
import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.domain.AcmeCertificateStatus
import fr.gouv.agora.domain.AcmeOrder
import fr.gouv.agora.domain.AcmeOrderStatus
import fr.gouv.agora.usecase.acme.repository.AcmeAccountRepository
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import fr.gouv.agora.usecase.acme.repository.AcmeOrderRepository
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
    private val orderRepository: AcmeOrderRepository,
    private val cloudflareDeployer: CloudflareCertificateDeployer,
    private val clock: Clock,
) {

    private val logger = LoggerFactory.getLogger(AcmeCertificateRenewalUseCase::class.java)

    companion object {
        private const val RENEWAL_THRESHOLD_DAYS = 30L
        private const val POLLING_MAX_ATTEMPTS = 10
        private const val POLLING_INTERVAL_MS = 3_000L
        private const val ORDER_EXPIRY_HOURS = 7 * 24L  // 168h = 7 jours (compatible avec validation OV longue)
    }

    fun renewIfNeeded() {
        if (!acmeConfig.enabled) {
            logger.info("ACME renewal is disabled (ACME_ENABLED=false). Skipping.")
            return
        }

        val startTime = System.currentTimeMillis()
        val domain = acmeConfig.domain
        val serverUrl = acmeConfig.serverUrl
        val now = LocalDateTime.now(clock)

        logger.info(
            "Starting ACME renewal check for domain=$domain " +
                "[acmeServerInteraction=${acmeConfig.acmeServerInteractionEnabled}, " +
                "cloudflareInteraction=${acmeConfig.cloudflareInteractionEnabled}]"
        )

        // 1. Vérification expiration + statut de déploiement
        val existingCert = certificateRepository.loadCertificate(domain)

        if (existingCert != null) {
            logger.info("Loaded existing certificate for $domain: status=${existingCert.status}, expiresAt=${existingCert.expiresAt}")
        } else {
            logger.info("No existing certificate found in database for $domain")
        }

        val needsProvisioning = when {
            existingCert == null -> {
                logger.info("No existing certificate found for $domain. Proceeding with first issuance.")
                true
            }
            existingCert.expiresAt.isAfter(now.plusDays(RENEWAL_THRESHOLD_DAYS)) &&
                existingCert.status == AcmeCertificateStatus.DEPLOYED -> {
                logger.info("Certificate for $domain is still valid until ${existingCert.expiresAt} and already deployed. No action needed.")
                return
            }
            existingCert.expiresAt.isAfter(now.plusDays(RENEWAL_THRESHOLD_DAYS)) &&
                existingCert.status == AcmeCertificateStatus.TO_DEPLOY -> {
                logger.info("Certificate for $domain is valid until ${existingCert.expiresAt} but was not yet deployed to Cloudflare. Retrying deployment.")
                false
            }
            else -> {
                logger.info("Certificate for $domain expires at ${existingCert.expiresAt}. Renewal needed.")
                true
            }
        }

        val certPem: String
        val domainPrivKeyPem: String

        try {
        if (needsProvisioning && !acmeConfig.acmeServerInteractionEnabled) {
            logger.info("ACME server interaction is disabled (ACME_SERVER_INTERACTION_ENABLED=false). Skipping certificate provisioning.")
            return
        }

        if (needsProvisioning) {
            // Reprise sur incident : vérifier si un order est déjà en cours en base
            val pendingOrder = orderRepository.loadOrder(domain)

            if (pendingOrder != null) {
                val orderAgeHours = java.time.Duration.between(pendingOrder.createdAt, now).toHours()
                if (orderAgeHours >= ORDER_EXPIRY_HOURS) {
                    logger.warn("Stale ACME order for $domain (created ${pendingOrder.createdAt}, age ${orderAgeHours}h > ${ORDER_EXPIRY_HOURS}h). Deleting and starting fresh.")
                    orderRepository.deleteOrder(domain)
                    val result = startNewOrder(domain, serverUrl)
                    certPem = result.first
                    domainPrivKeyPem = result.second
                } else {
                    logger.info("Found pending ACME order for $domain with status ${pendingOrder.status}. Attempting resume.")
                    val result = resumeOrder(pendingOrder, domain, serverUrl)
                    if (result == null) {
                        logger.info("ACME challenge still pending for $domain. No further action this run. Will retry on next scheduled execution.")
                        return
                    }
                    certPem = result.first
                    domainPrivKeyPem = result.second
                }
            } else {
                val result = startNewOrder(domain, serverUrl)
                certPem = result.first
                domainPrivKeyPem = result.second
            }
        } else {
            // Pas de nouveau provisioning : on réutilise le certificat déjà en base
            certPem = existingCert!!.certificatePem
            domainPrivKeyPem = existingCert.privateKeyPem
        }

        // Déploiement Cloudflare
        if (!acmeConfig.cloudflareInteractionEnabled) {
            logger.info("Cloudflare interaction is disabled (ACME_CLOUDFLARE_INTERACTION_ENABLED=false). Skipping certificate deployment to Cloudflare.")
            return
        }

        cloudflareDeployer.deployCertificate(certPem, domainPrivKeyPem)

        // Mise à jour du statut en base → DEPLOYED + nettoyage de l'order
        certificateRepository.markAsDeployed(domain, LocalDateTime.now(clock))
        logger.info("Certificate status updated to DEPLOYED in database for $domain")
        orderRepository.deleteOrder(domain)
        logger.info("ACME order cleaned up from database for $domain")

        val durationMs = System.currentTimeMillis() - startTime
        logger.info("ACME certificate deployed successfully to Cloudflare for domain $domain in ${durationMs}ms.")
        } catch (e: Exception) {
            val durationMs = System.currentTimeMillis() - startTime
            logger.error("ACME renewal failed for domain $domain after ${durationMs}ms: ${e.message}", e)
            throw e
        }
    }

    private fun startNewOrder(domain: String, serverUrl: String): Pair<String, String> {
        logger.info("Starting new ACME order for domain $domain")

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
            val hasEab = acmeConfig.eabKid.isNotBlank() && acmeConfig.eabHmacKey.isNotBlank()
            if (hasEab) {
                logger.info("Creating new ACME account with EAB credentials.")
                accountBuilder.withKeyIdentifier(acmeConfig.eabKid, acmeConfig.eabHmacKey)
            } else {
                logger.info("Creating new ACME account without EAB (stub/test mode).")
            }
            accountBuilder
                .agreeToTermsOfService()
                .create(session)
        }

        // Persistance immédiate du compte ACME (avant challenge.trigger) pour éviter la perte
        // de la keypair en cas d'exception pendant le flux ACME (DEBT-02)
        val accountKeyWriter = StringWriter()
        KeyPairUtils.writeKeyPair(accountKeyPair, accountKeyWriter)
        accountRepository.saveAccount(
            AcmeAccount(
                serverUrl = serverUrl,
                accountUrl = account.location?.toString(),
                keyPem = accountKeyWriter.toString(),
            )
        )
        logger.info("ACME account persisted early for $serverUrl (accountUrl=${account.location})")

        // 5. Order
        val order = account.newOrder().domain(domain).create()

        // 6. Challenge HTTP-01
        val authorization = order.authorizations.first()
        val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
            ?: throw IllegalStateException("No HTTP-01 challenge available for domain $domain")

        // Persistance du challenge en base (partagé entre toutes les instances)
        challengeStore.storeChallenge(challenge.token, challenge.authorization)

        // 9. Keypair domaine + CSR (généré maintenant pour pouvoir persister la clé avant le polling)
        val domainKeyPair = KeyPairUtils.createKeyPair(2048)
        val domainPrivKeyWriter = StringWriter()
        KeyPairUtils.writeKeyPair(domainKeyPair, domainPrivKeyWriter)
        val domainPrivKeyPem = domainPrivKeyWriter.toString()

        // Persistance de l'order en base avant le polling (reprise possible en cas d'incident)
        orderRepository.saveOrder(
            AcmeOrder(
                domain = domain,
                orderUrl = order.location.toString(),
                domainKeyPem = domainPrivKeyPem,
                status = AcmeOrderStatus.CHALLENGE_PENDING,
                createdAt = LocalDateTime.now(clock),
            )
        )

        try {
            // 7. Trigger validation
            challenge.trigger()

            // 8. Polling jusqu'à VALID
            pollUntilChallengeValid(challenge, domain)

            // Nettoyage du challenge uniquement en cas de succès
            challengeStore.clearChallenge(challenge.token)
        } catch (e: AcmeChallengeTimeoutException) {
            // Ne pas effacer le challenge : Sectigo peut valider plus tard.
            // L'order reste en base (statut CHALLENGE_PENDING) pour reprise lors du prochain run.
            logger.warn("Challenge timed out for $domain. Leaving challenge token in store for next retry run.")
            throw e
        } catch (e: Exception) {
            challengeStore.clearChallenge(challenge.token)
            throw e
        }

        // Mise à jour statut order → ORDER_FINALIZING
        orderRepository.updateOrderStatus(domain, AcmeOrderStatus.ORDER_FINALIZING)

        // 10. Finalisation Order
        val csrBuilder = CSRBuilder()
        csrBuilder.addDomain(domain)
        csrBuilder.sign(domainKeyPair)
        order.execute(csrBuilder.encoded)

        pollUntilOrderValid(order, domain)

        // 11. Téléchargement certificat
        val certPem = downloadCertificate(order, domain)

        // 15. Persistance certificat (statut TO_DEPLOY)
        val x509Cert = order.certificate!!.certificateChain.first() as X509Certificate
        val expiresAt = x509Cert.notAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        certificateRepository.saveCertificate(
            AcmeCertificate(
                domain = domain,
                certificatePem = certPem,
                privateKeyPem = domainPrivKeyPem,
                expiresAt = expiresAt,
                status = AcmeCertificateStatus.TO_DEPLOY,
            )
        )

        return Pair(certPem, domainPrivKeyPem)
    }

    private fun resumeOrder(pendingOrder: AcmeOrder, domain: String, serverUrl: String): Pair<String, String>? {
        logger.info("Resuming ACME order for $domain at status ${pendingOrder.status} (orderUrl=${pendingOrder.orderUrl})")

        val storedAccount = accountRepository.loadAccount(serverUrl)
            ?: throw IllegalStateException("Cannot resume ACME order: no account found for $serverUrl")

        val accountKeyPair = KeyPairUtils.readKeyPair(StringReader(storedAccount.keyPem))
        val session = Session(serverUrl)

        val accountUrl = storedAccount.accountUrl
            ?: throw IllegalStateException("Cannot resume ACME order: account has no URL for $serverUrl")

        val login = session.login(java.net.URL(accountUrl), accountKeyPair)
        val order = login.bindOrder(java.net.URL(pendingOrder.orderUrl))
        order.update()

        val domainPrivKeyPem = pendingOrder.domainKeyPem

        return when (pendingOrder.status) {
            AcmeOrderStatus.CHALLENGE_PENDING -> {
                logger.info("Resuming from CHALLENGE_PENDING: checking challenge status for $domain")

                val authorization = order.authorizations.first()
                try {
                    authorization.update()
                } catch (e: org.shredzone.acme4j.exception.AcmeRetryAfterException) {
                    // Sectigo indique que l'authorization n'est pas encore traitée (Retry-After).
                    // On sort proprement : l'order reste en base (CHALLENGE_PENDING), le prochain run re-vérifiera.
                    logger.info(
                        "Authorization not completed yet for $domain (Retry-After: ${e.retryAfter}). " +
                            "Will retry on next scheduled execution."
                    )
                    return null
                }
                val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
                    ?: throw IllegalStateException("No HTTP-01 challenge available for domain $domain during resume")

                // Re-stocker le challenge pour que l'endpoint HTTP-01 reste disponible côté Sectigo
                challengeStore.storeChallenge(challenge.token, challenge.authorization)

                when (challenge.status) {
                    org.shredzone.acme4j.Status.VALID -> {
                        logger.info("Challenge already VALID for $domain. Proceeding to order finalization.")
                        challengeStore.clearChallenge(challenge.token)

                        orderRepository.updateOrderStatus(domain, AcmeOrderStatus.ORDER_FINALIZING)

                        val domainKeyPair = KeyPairUtils.readKeyPair(StringReader(domainPrivKeyPem))
                        val csrBuilder = CSRBuilder()
                        csrBuilder.addDomain(domain)
                        csrBuilder.sign(domainKeyPair)
                        order.execute(csrBuilder.encoded)

                        pollUntilOrderValid(order, domain)
                        val certPem = downloadAndPersistCertificate(order, domain, domainPrivKeyPem)
                        Pair(certPem, domainPrivKeyPem)
                    }

                    org.shredzone.acme4j.Status.INVALID -> {
                        logger.error("Challenge INVALID for $domain during resume. Order cannot be completed.")
                        challengeStore.clearChallenge(challenge.token)
                        throw AcmeChallengeFailedException("ACME HTTP-01 challenge INVALID for domain $domain during resume")
                    }

                    else -> {
                        // PENDING ou PROCESSING : Sectigo n'a pas encore validé.
                        // On re-trigger au cas où pour signaler notre disponibilité, puis on sort proprement.
                        // L'order reste en base (CHALLENGE_PENDING), la prochaine exécution re-vérifiera.
                        logger.info(
                            "Challenge status is ${challenge.status} for $domain. " +
                                "Sectigo has not yet validated. Will retry on next scheduled execution."
                        )
                        if (challenge.status != org.shredzone.acme4j.Status.PROCESSING) {
                            logger.info("Re-triggering challenge for $domain to notify Sectigo of token availability.")
                            challenge.trigger()
                        }
                        null
                    }
                }
            }

            AcmeOrderStatus.ORDER_FINALIZING -> {
                logger.info("Resuming from ORDER_FINALIZING: polling order status for $domain")

                pollUntilOrderValid(order, domain)
                val certPem = downloadAndPersistCertificate(order, domain, domainPrivKeyPem)
                Pair(certPem, domainPrivKeyPem)
            }
        }
    }

    private fun pollUntilChallengeValid(challenge: Http01Challenge, domain: String) {
        repeat(POLLING_MAX_ATTEMPTS) { attempt ->
            Thread.sleep(POLLING_INTERVAL_MS)
            challenge.update()
            when (challenge.status) {
                org.shredzone.acme4j.Status.VALID -> {
                    logger.info("ACME HTTP-01 challenge VALID after ${attempt + 1} attempt(s) for domain $domain")
                    return
                }
                org.shredzone.acme4j.Status.INVALID -> {
                    throw AcmeChallengeFailedException("ACME HTTP-01 challenge INVALID for domain $domain")
                }
                else -> logger.info("Challenge status: ${challenge.status} (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
            }
            if (attempt == POLLING_MAX_ATTEMPTS - 1) {
                throw AcmeChallengeTimeoutException("ACME HTTP-01 challenge timed out after $POLLING_MAX_ATTEMPTS attempts for domain $domain")
            }
        }
    }

    private fun pollUntilOrderValid(order: org.shredzone.acme4j.Order, domain: String) {
        repeat(POLLING_MAX_ATTEMPTS) { attempt ->
            Thread.sleep(POLLING_INTERVAL_MS)
            order.update()
            when (order.status) {
                org.shredzone.acme4j.Status.VALID -> {
                    logger.info("ACME order VALID after ${attempt + 1} attempt(s) for domain $domain")
                    return
                }
                org.shredzone.acme4j.Status.INVALID -> throw AcmeChallengeFailedException("ACME order became INVALID for domain $domain")
                else -> logger.info("Order status: ${order.status} (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
            }
        }
        throw AcmeChallengeTimeoutException("ACME order timed out after $POLLING_MAX_ATTEMPTS attempts for domain $domain")
    }

    private fun downloadCertificate(order: org.shredzone.acme4j.Order, domain: String): String {
        val certificate = order.certificate ?: throw IllegalStateException("ACME order completed but certificate is null for domain $domain")
        val certChain = certificate.certificateChain
        return buildString {
            certChain.forEach { cert ->
                append("-----BEGIN CERTIFICATE-----\n")
                append(Base64.getMimeEncoder(64, "\n".toByteArray()).encodeToString(cert.encoded))
                append("\n-----END CERTIFICATE-----\n")
            }
        }
    }

    private fun downloadAndPersistCertificate(
        order: org.shredzone.acme4j.Order,
        domain: String,
        domainPrivKeyPem: String,
    ): String {
        val certPem = downloadCertificate(order, domain)
        val x509Cert = order.certificate!!.certificateChain.first() as X509Certificate
        val expiresAt = x509Cert.notAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        certificateRepository.saveCertificate(
            AcmeCertificate(
                domain = domain,
                certificatePem = certPem,
                privateKeyPem = domainPrivKeyPem,
                expiresAt = expiresAt,
                status = AcmeCertificateStatus.TO_DEPLOY,
            )
        )
        return certPem
    }
}

class AcmeChallengeFailedException(message: String) : RuntimeException(message)
class AcmeChallengeTimeoutException(message: String) : RuntimeException(message)
