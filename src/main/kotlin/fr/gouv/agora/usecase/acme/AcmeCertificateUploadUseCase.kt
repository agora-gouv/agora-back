package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.domain.AcmeCertificateStatus
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AcmeCertificateUploadUseCase(
    private val acmeConfig: AcmeConfig,
    private val certificateRepository: AcmeCertificateRepository,
) {

    private val logger = LoggerFactory.getLogger(AcmeCertificateUploadUseCase::class.java)

    fun uploadCertificate(certificatePem: String, privateKeyPem: String, expiresAt: LocalDateTime) {
        val domain = acmeConfig.domain
        logger.info("Uploading manually provided certificate for domain $domain, expires at $expiresAt")

        certificateRepository.saveCertificate(
            AcmeCertificate(
                domain = domain,
                certificatePem = certificatePem,
                privateKeyPem = privateKeyPem,
                expiresAt = expiresAt,
                status = AcmeCertificateStatus.TO_DEPLOY,
            )
        )

        logger.info("Certificate for domain $domain saved with status TO_DEPLOY. It will be deployed to Cloudflare on the next renewIfNeeded() call.")
    }
}
