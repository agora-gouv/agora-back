package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeCertificateStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface AcmeCertificateJpaRepository : JpaRepository<AcmeCertificateDAO, UUID> {
    fun findFirstByDomainOrderByCreatedAtDesc(domain: String): AcmeCertificateDAO?

    @Modifying
    @Transactional
    @Query(
        """
        UPDATE AcmeCertificateDAO c
        SET c.status = :status, c.deployedAt = :deployedAt
        WHERE c.id = (
            SELECT c2.id FROM AcmeCertificateDAO c2
            WHERE c2.domain = :domain
            ORDER BY c2.createdAt DESC
            LIMIT 1
        )
        """
    )
    fun markAsDeployedForLatestByDomain(domain: String, status: AcmeCertificateStatus, deployedAt: LocalDateTime): Int
}
