package fr.social.gouv.agora.infrastructure.moderatus.repository

import fr.social.gouv.agora.infrastructure.moderatus.dto.ModeratusQagLockDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface ModeratusQagLockDatabaseRepository : JpaRepository<ModeratusQagLockDTO, UUID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM moderatus_locked_qags WHERE qag_id = :qagId", nativeQuery = true)
    fun deleteLockedQag(@Param("qagId") qagId: UUID): Int

}