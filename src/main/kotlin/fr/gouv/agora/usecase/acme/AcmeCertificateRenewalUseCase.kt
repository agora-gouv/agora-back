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

        logger.info("Deploying certificate to Cloudflare for $domain...")
        cloudflareDeployer.deployCertificate(certPem, domainPrivKeyPem)
        logger.info("Certificate successfully deployed to Cloudflare for $domain")

        // Mise à jour du statut en base → DEPLOYED + nettoyage de l'order
        certificateRepository.markAsDeployed(domain, LocalDateTime.now(clock))
        logger.info("Certificate status updated to DEPLOYED in database for $domain")
        orderRepository.deleteOrder(domain)
        logger.info("ACME order cleaned up from database for $domain")

        val durationMs = System.currentTimeMillis() - startTime
        logger.info("ACME certificate renewal completed successfully for domain $domain in ${durationMs}ms.")
        } catch (e: Exception) {
            val durationMs = System.currentTimeMillis() - startTime
            logger.error("ACME renewal failed for domain $domain after ${durationMs}ms: ${e.message}", e)
            throw e
        }
    }

    private fun startNewOrder(domain: String, serverUrl: String): Pair<String, String> {
        logger.info("=== [startNewOrder] Starting new ACME order for domain=$domain, serverUrl=$serverUrl ===")

        // 2. Chargement/création keypair compte
        val storedAccount = accountRepository.loadAccount(serverUrl)
        val accountKeyPair: KeyPair = if (storedAccount != null) {
            logger.info("[startNewOrder] Existing ACME account found for $serverUrl (accountUrl=${storedAccount.accountUrl}). Loading key pair.")
            KeyPairUtils.readKeyPair(StringReader(storedAccount.keyPem))
        } else {
            logger.info("[startNewOrder] No ACME account found for $serverUrl. Generating new RSA-2048 key pair.")
            KeyPairUtils.createKeyPair(2048)
        }
        logger.info("[startNewOrder] Account key pair ready for $serverUrl")

        // 3. Session acme4j
        logger.info("[startNewOrder] Creating ACME session for $serverUrl")
        val session = Session(serverUrl)
        logger.info("[startNewOrder] ACME session created for $serverUrl")

        // 4. Compte ACME
        val accountBuilder = AccountBuilder().useKeyPair(accountKeyPair)
        val account = if (storedAccount?.accountUrl != null) {
            logger.info("[startNewOrder] Reconnecting to existing ACME account: ${storedAccount.accountUrl}")
            val acc = accountBuilder.onlyExisting().create(session)
            logger.info("[startNewOrder] Reconnected to existing ACME account: location=${acc.location}, status=${acc.status}")
            acc
        } else {
            val hasEab = acmeConfig.eabKid.isNotBlank() && acmeConfig.eabHmacKey.isNotBlank()
            if (hasEab) {
                logger.info("[startNewOrder] Creating new ACME account with EAB credentials (kid=${acmeConfig.eabKid}).")
                accountBuilder.withKeyIdentifier(acmeConfig.eabKid, acmeConfig.eabHmacKey)
            } else {
                logger.info("[startNewOrder] Creating new ACME account without EAB (stub/test mode).")
            }
            val acc = accountBuilder
                .agreeToTermsOfService()
                .create(session)
            logger.info("[startNewOrder] New ACME account created: location=${acc.location}, status=${acc.status}")
            acc
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
        logger.info("[startNewOrder] ACME account persisted to database (serverUrl=$serverUrl, accountUrl=${account.location})")

        // 5. Order
        logger.info("[startNewOrder] Creating new ACME order for domain=$domain on server $serverUrl")
        val order = account.newOrder().domain(domain).create()
        logger.info("[startNewOrder] ACME order created: orderUrl=${order.location}, status=${order.status}, expires=${order.expires}")

        // 6. Challenge HTTP-01
        logger.info("[startNewOrder] Fetching authorization for domain=$domain")
        val authorization = order.authorizations.first()
        logger.info(
            "[startNewOrder] Authorization retrieved: identifier=${authorization.identifier}, " +
                "status=${authorization.status}, expires=${authorization.expires}"
        )

        val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
            ?: throw IllegalStateException("No HTTP-01 challenge available for domain $domain")
        logger.info("[startNewOrder] HTTP-01 challenge found: token=${challenge.token}, challengeUrl=${challenge.location}, status=${challenge.status}")

        // Persistance du challenge en base (partagé entre toutes les instances)
        challengeStore.storeChallenge(challenge.token, challenge.authorization)
        logger.info("[startNewOrder] Challenge token stored in database for $domain (token=${challenge.token})")

        // 9. Keypair domaine + CSR (généré maintenant pour pouvoir persister la clé avant le polling)
        logger.info("[startNewOrder] Generating domain key pair for $domain")
        val domainKeyPair = KeyPairUtils.createKeyPair(2048)
        val domainPrivKeyWriter = StringWriter()
        KeyPairUtils.writeKeyPair(domainKeyPair, domainPrivKeyWriter)
        val domainPrivKeyPem = domainPrivKeyWriter.toString()
        logger.info("[startNewOrder] Domain key pair generated for $domain")

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
        logger.info("[startNewOrder] Order persisted to database for $domain (status=CHALLENGE_PENDING, orderUrl=${order.location})")

        try {
            // 7. Trigger validation
            logger.info("[startNewOrder] Triggering HTTP-01 challenge for $domain (challengeUrl=${challenge.location})...")
            challenge.trigger()
            logger.info("[startNewOrder] HTTP-01 challenge triggered successfully for $domain. Waiting for ACME server validation...")

            // 8. Polling jusqu'à VALID
            pollUntilChallengeValid(challenge, domain)

            // Nettoyage du challenge uniquement en cas de succès
            challengeStore.clearChallenge(challenge.token)
            logger.info("[startNewOrder] Challenge token cleared from store for $domain (token=${challenge.token})")
        } catch (e: AcmeChallengeTimeoutException) {
            // Ne pas effacer le challenge : Sectigo peut valider plus tard.
            // L'order reste en base (statut CHALLENGE_PENDING) pour reprise lors du prochain run.
            logger.warn("[startNewOrder] Challenge timed out for $domain. Leaving challenge token in store for next retry run.")
            throw e
        } catch (e: Exception) {
            challengeStore.clearChallenge(challenge.token)
            logger.info("[startNewOrder] Challenge token cleared from store after error for $domain (token=${challenge.token})")
            throw e
        }

        // Mise à jour statut order → ORDER_FINALIZING
        orderRepository.updateOrderStatus(domain, AcmeOrderStatus.ORDER_FINALIZING)
        logger.info("[startNewOrder] Order status updated to ORDER_FINALIZING in database for $domain")

        // 10. Finalisation Order
        logger.info("[startNewOrder] Generating CSR for domain=$domain")
        val csrBuilder = CSRBuilder()
        csrBuilder.addDomain(domain)
        csrBuilder.sign(domainKeyPair)
        logger.info("[startNewOrder] CSR generated for $domain")

        // Recharge le statut de l'order depuis le serveur ACME avant finalisation (RFC 8555 §7.4 : l'order
        // doit être en statut "ready" pour accepter le CSR). acme4j ne recharge pas automatiquement l'état
        // local après le polling du challenge.
        order.update()
        logger.info("[startNewOrder] Order status before CSR submission for $domain: status=${order.status}, expires=${order.expires}")

        logger.info("[startNewOrder] Submitting CSR to ACME server for $domain (orderUrl=${order.location})...")
        order.execute(csrBuilder.encoded)
        logger.info("[startNewOrder] CSR submitted for $domain. Polling for order finalization...")

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
        logger.info("[startNewOrder] Certificate saved to database for $domain (status=TO_DEPLOY, expiresAt=$expiresAt)")

        logger.info("=== [startNewOrder] New ACME order completed for domain=$domain (expiresAt=$expiresAt) ===")
        return Pair(certPem, domainPrivKeyPem)
    }

    private fun resumeOrder(pendingOrder: AcmeOrder, domain: String, serverUrl: String): Pair<String, String>? {
        logger.info("=== [resumeOrder] Resuming ACME order for domain=$domain at status=${pendingOrder.status} (orderUrl=${pendingOrder.orderUrl}) ===")

        val storedAccount = accountRepository.loadAccount(serverUrl)
            ?: throw IllegalStateException("Cannot resume ACME order: no account found for $serverUrl")

        logger.info("[resumeOrder] Loading ACME account key pair for $serverUrl (accountUrl=${storedAccount.accountUrl})")
        val accountKeyPair = KeyPairUtils.readKeyPair(StringReader(storedAccount.keyPem))

        logger.info("[resumeOrder] Creating ACME session for $serverUrl")
        val session = Session(serverUrl)

        val accountUrl = storedAccount.accountUrl
            ?: throw IllegalStateException("Cannot resume ACME order: account has no URL for $serverUrl")

        logger.info("[resumeOrder] Reconnecting to ACME account at $accountUrl")
        val login = session.login(java.net.URL(accountUrl), accountKeyPair)
        logger.info("[resumeOrder] Successfully logged in to ACME account at $accountUrl")

        logger.info("[resumeOrder] Binding order from URL: ${pendingOrder.orderUrl}")
        val order = login.bindOrder(java.net.URL(pendingOrder.orderUrl))
        order.update()
        logger.info("[resumeOrder] Order refreshed from ACME server: status=${order.status}, expires=${order.expires}")

        val domainPrivKeyPem = pendingOrder.domainKeyPem

        return when (pendingOrder.status) {
            AcmeOrderStatus.CHALLENGE_PENDING -> {
                logger.info("[resumeOrder] Resuming from CHALLENGE_PENDING: checking challenge status for $domain")

                val authorization = order.authorizations.first()
                logger.info(
                    "[resumeOrder] Authorization state before update: identifier=${authorization.identifier}, " +
                        "status=${authorization.status}, expires=${authorization.expires}"
                )

                // Récupération et re-stockage défensif du challenge AVANT authorization.update().
                // Cela couvre le cas où la ligne acme_challenge a disparu de la base (perte accidentelle,
                // redémarrage, etc.) : le token et la keyAuthorization sont restaurés immédiatement,
                // même si Sectigo répond ensuite avec un Retry-After (qui ferait sortir avant le storeChallenge
                // si celui-ci était placé après authorization.update()).
                val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
                    ?: throw IllegalStateException("No HTTP-01 challenge available for domain $domain during resume")
                logger.info("[resumeOrder] HTTP-01 challenge found: token=${challenge.token}, status=${challenge.status}, challengeUrl=${challenge.location}")

                logger.info("[resumeOrder] Restoring challenge token in store for $domain (defensive re-store before authorization.update(), token=${challenge.token})")
                challengeStore.storeChallenge(challenge.token, challenge.authorization)
                logger.info("[resumeOrder] Challenge token restored in store for $domain")

                try {
                    logger.info("[resumeOrder] Calling authorization.update() for $domain...")
                    authorization.update()
                    logger.info(
                        "[resumeOrder] Authorization updated for $domain: status=${authorization.status}, " +
                            "expires=${authorization.expires}"
                    )
                } catch (e: org.shredzone.acme4j.exception.AcmeRetryAfterException) {
                    // Sectigo indique que l'authorization n'est pas encore traitée (Retry-After).
                    // Le challenge est déjà re-stocké en base (ci-dessus) : Sectigo peut valider.
                    // On sort proprement : l'order reste en base (CHALLENGE_PENDING), le prochain run re-vérifiera.
                    logger.info(
                        "[resumeOrder] Authorization not completed yet for $domain (Retry-After: ${e.retryAfter}). " +
                            "Will retry on next scheduled execution."
                    )
                    return null
                }

                logger.info("[resumeOrder] Challenge status after authorization.update() for $domain: ${challenge.status}")

                when (challenge.status) {
                    org.shredzone.acme4j.Status.VALID -> {
                        logger.info("[resumeOrder] Challenge already VALID for $domain. Proceeding to order finalization.")
                        challengeStore.clearChallenge(challenge.token)
                        logger.info("[resumeOrder] Challenge token cleared from store for $domain (token=${challenge.token})")

                        orderRepository.updateOrderStatus(domain, AcmeOrderStatus.ORDER_FINALIZING)
                        logger.info("[resumeOrder] Order status updated to ORDER_FINALIZING in database for $domain")

                        logger.info("[resumeOrder] Generating CSR for domain=$domain")
                        val domainKeyPair = KeyPairUtils.readKeyPair(StringReader(domainPrivKeyPem))
                        val csrBuilder = CSRBuilder()
                        csrBuilder.addDomain(domain)
                        csrBuilder.sign(domainKeyPair)
                        logger.info("[resumeOrder] CSR generated for $domain")

                        logger.info("[resumeOrder] Submitting CSR to ACME server for $domain (orderUrl=${order.location})...")
                        order.execute(csrBuilder.encoded)
                        logger.info("[resumeOrder] CSR submitted for $domain. Polling for order finalization...")

                        pollUntilOrderValid(order, domain)
                        val certPem = downloadAndPersistCertificate(order, domain, domainPrivKeyPem)
                        Pair(certPem, domainPrivKeyPem)
                    }

                    org.shredzone.acme4j.Status.INVALID -> {
                        logger.error(
                            "[resumeOrder] Challenge INVALID for $domain during resume. Order cannot be completed. " +
                                "error.type=${challenge.error?.type}, error.detail=${challenge.error?.detail}"
                        )
                        challengeStore.clearChallenge(challenge.token)
                        logger.info("[resumeOrder] Challenge token cleared from store after INVALID for $domain")
                        throw AcmeChallengeFailedException("ACME HTTP-01 challenge INVALID for domain $domain during resume")
                    }

                    else -> {
                        // PENDING ou PROCESSING : Sectigo n'a pas encore validé.
                        // On re-trigger au cas où pour signaler notre disponibilité, puis on sort proprement.
                        // L'order reste en base (CHALLENGE_PENDING), la prochaine exécution re-vérifiera.
                        logger.info(
                            "[resumeOrder] Challenge status is ${challenge.status} for $domain. " +
                                "Sectigo has not yet validated. Will retry on next scheduled execution."
                        )
                        if (challenge.status != org.shredzone.acme4j.Status.PROCESSING) {
                            logger.info("[resumeOrder] Re-triggering challenge for $domain (challengeUrl=${challenge.location}) to notify Sectigo of token availability.")
                            challenge.trigger()
                            logger.info("[resumeOrder] Challenge re-triggered for $domain.")
                        } else {
                            logger.info("[resumeOrder] Challenge is PROCESSING for $domain — skipping re-trigger, Sectigo is already working on it.")
                        }
                        null
                    }
                }
            }

            AcmeOrderStatus.ORDER_FINALIZING -> {
                logger.info("[resumeOrder] Resuming from ORDER_FINALIZING: polling order status for $domain (orderUrl=${order.location})")
                pollUntilOrderValid(order, domain)
                val certPem = downloadAndPersistCertificate(order, domain, domainPrivKeyPem)
                Pair(certPem, domainPrivKeyPem)
            }
        }
    }

    private fun pollUntilChallengeValid(challenge: Http01Challenge, domain: String) {
        logger.info("[pollChallenge] Starting challenge polling for $domain (max ${POLLING_MAX_ATTEMPTS} attempts × ${POLLING_INTERVAL_MS}ms)")
        repeat(POLLING_MAX_ATTEMPTS) { attempt ->
            logger.info("[pollChallenge] Waiting ${POLLING_INTERVAL_MS}ms before attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS for $domain...")
            Thread.sleep(POLLING_INTERVAL_MS)
            logger.info("[pollChallenge] Calling challenge.update() for $domain (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
            challenge.update()
            logger.info("[pollChallenge] Challenge status after update: ${challenge.status} (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS, domain=$domain)")
            when (challenge.status) {
                org.shredzone.acme4j.Status.VALID -> {
                    logger.info("[pollChallenge] HTTP-01 challenge VALID after ${attempt + 1} attempt(s) for domain $domain ✓")
                    return
                }
                org.shredzone.acme4j.Status.INVALID -> {
                    logger.error(
                        "[pollChallenge] HTTP-01 challenge INVALID for domain $domain: " +
                            "error.type=${challenge.error?.type}, error.detail=${challenge.error?.detail}"
                    )
                    throw AcmeChallengeFailedException("ACME HTTP-01 challenge INVALID for domain $domain")
                }
                else -> logger.info("[pollChallenge] Challenge still ${challenge.status}, continuing polling... (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
            }
            if (attempt == POLLING_MAX_ATTEMPTS - 1) {
                logger.warn("[pollChallenge] Challenge polling exhausted for $domain after $POLLING_MAX_ATTEMPTS attempts. Last status: ${challenge.status}")
                throw AcmeChallengeTimeoutException("ACME HTTP-01 challenge timed out after $POLLING_MAX_ATTEMPTS attempts for domain $domain")
            }
        }
    }

    private fun pollUntilOrderValid(order: org.shredzone.acme4j.Order, domain: String) {
        logger.info("[pollOrder] Starting order polling for $domain (max ${POLLING_MAX_ATTEMPTS} attempts × ${POLLING_INTERVAL_MS}ms, orderUrl=${order.location})")
        repeat(POLLING_MAX_ATTEMPTS) { attempt ->
            logger.info("[pollOrder] Waiting ${POLLING_INTERVAL_MS}ms before attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS for $domain...")
            Thread.sleep(POLLING_INTERVAL_MS)
            logger.info("[pollOrder] Calling order.update() for $domain (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS)")
            try {
                order.update()
            } catch (e: org.shredzone.acme4j.exception.AcmeRetryAfterException) {
                // Sectigo indique que l'order n'est pas encore prêt (header Retry-After).
                // On continue le polling plutôt que de laisser l'exception remonter.
                logger.info(
                    "[pollOrder] Order not ready yet for $domain (Retry-After: ${e.retryAfter}), " +
                        "attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS — continuing polling."
                )
                return@repeat
            }
            logger.info("[pollOrder] Order status after update: ${order.status} (attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS, domain=$domain, expires=${order.expires})")
            when (order.status) {
                org.shredzone.acme4j.Status.VALID -> {
                    logger.info("[pollOrder] ACME order VALID after ${attempt + 1} attempt(s) for domain $domain ✓")
                    return
                }
                org.shredzone.acme4j.Status.INVALID -> {
                    logger.error(
                        "[pollOrder] ACME order INVALID for domain $domain: " +
                            "error.type=${order.error?.type}, error.detail=${order.error?.detail}"
                    )
                    throw AcmeChallengeFailedException("ACME order became INVALID for domain $domain")
                }
                else -> logger.info(
                    "[pollOrder] Order still ${order.status}, continuing polling... " +
                        "(attempt ${attempt + 1}/$POLLING_MAX_ATTEMPTS, expires=${order.expires}, error=${order.error?.detail})"
                )
            }
        }
        logger.warn("[pollOrder] Order polling exhausted for $domain after $POLLING_MAX_ATTEMPTS attempts. Last status: ${order.status}")
        throw AcmeChallengeTimeoutException("ACME order timed out after $POLLING_MAX_ATTEMPTS attempts for domain $domain")
    }

    private fun downloadCertificate(order: org.shredzone.acme4j.Order, domain: String): String {
        logger.info("[downloadCert] Downloading certificate from ACME server for $domain (orderUrl=${order.location})")
        val certificate = order.certificate ?: throw IllegalStateException("ACME order completed but certificate is null for domain $domain")
        val certChain = certificate.certificateChain
        logger.info("[downloadCert] Certificate chain received for $domain: ${certChain.size} certificate(s) in chain")
        val pem = buildString {
            certChain.forEach { cert ->
                append("-----BEGIN CERTIFICATE-----\n")
                append(Base64.getMimeEncoder(64, "\n".toByteArray()).encodeToString(cert.encoded))
                append("\n-----END CERTIFICATE-----\n")
            }
        }
        val leafCert = certChain.first() as X509Certificate
        logger.info(
            "[downloadCert] Certificate downloaded for $domain: " +
                "subject=${leafCert.subjectX500Principal.name}, " +
                "notBefore=${leafCert.notBefore}, notAfter=${leafCert.notAfter}, " +
                "issuer=${leafCert.issuerX500Principal.name}"
        )
        return pem
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
        logger.info("[downloadCert] Certificate saved to database for $domain (status=TO_DEPLOY, expiresAt=$expiresAt)")
        return certPem
    }
}

class AcmeChallengeFailedException(message: String) : RuntimeException(message)
class AcmeChallengeTimeoutException(message: String) : RuntimeException(message)
