package fr.gouv.agora.infrastructure.acme.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AcmeCertificateJpaRepository : JpaRepository<AcmeCertificateDAO, UUID> {
    fun findFirstByDomainOrderByCreatedAtDesc(domain: String): AcmeCertificateDAO?
}
