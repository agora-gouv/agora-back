package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeOrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface AcmeOrderJpaRepository : JpaRepository<AcmeOrderDAO, UUID> {
    fun findFirstByDomainOrderByCreatedAtDesc(domain: String): AcmeOrderDAO?

    @Modifying
    @Transactional
    @Query(
        """
        UPDATE AcmeOrderDAO o
        SET o.status = :status
        WHERE o.id = (
            SELECT o2.id FROM AcmeOrderDAO o2
            WHERE o2.domain = :domain
            ORDER BY o2.createdAt DESC
            LIMIT 1
        )
        """
    )
    fun updateStatusForLatestByDomain(domain: String, status: AcmeOrderStatus): Int

    @Modifying
    @Transactional
    fun deleteByDomain(domain: String): Int
}
