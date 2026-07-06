package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeCertificate
import fr.gouv.agora.usecase.acme.repository.AcmeCertificateRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AcmeCertificateRepositoryImpl(
    private val jpaRepository: AcmeCertificateJpaRepository,
    private val cryptoHelper: AcmeCryptoHelper,
) : AcmeCertificateRepository {

    override fun loadCertificate(domain: String): AcmeCertificate? {
        val dao = jpaRepository.findFirstByDomainOrderByCreatedAtDesc(domain) ?: return null
        return AcmeCertificate(
            domain = dao.domain,
            certificatePem = dao.certificate,
            privateKeyPem = cryptoHelper.decrypt(dao.privateKey),
            expiresAt = dao.expiresAt,
        )
    }

    override fun saveCertificate(certificate: AcmeCertificate) {
        val dao = AcmeCertificateDAO(
            domain = certificate.domain,
            certificate = certificate.certificatePem,
            privateKey = cryptoHelper.encrypt(certificate.privateKeyPem),
            expiresAt = certificate.expiresAt,
            createdAt = LocalDateTime.now(),
        )
        jpaRepository.save(dao)
    }
}
