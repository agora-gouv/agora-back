package fr.gouv.agora.infrastructure.acme.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AcmeAccountJpaRepository : JpaRepository<AcmeAccountDAO, UUID> {
    fun findFirstByServerUrlOrderByCreatedAtDesc(serverUrl: String): AcmeAccountDAO?
}
