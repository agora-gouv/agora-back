package fr.gouv.agora.infrastructure.acme.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AcmeChallengeJpaRepository : JpaRepository<AcmeChallengeDAO, String>
