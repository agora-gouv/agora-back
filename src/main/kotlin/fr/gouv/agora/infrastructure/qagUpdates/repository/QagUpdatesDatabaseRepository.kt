package fr.gouv.agora.infrastructure.qagUpdates.repository

import fr.gouv.agora.infrastructure.qagUpdates.dto.QagUpdatesDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagUpdatesDatabaseRepository : JpaRepository<QagUpdatesDTO, UUID> {

    @Query(value = "SELECT * FROM qag_updates WHERE qag_id in :qagUUIDList", nativeQuery = true)
    fun getQagUpdates(@Param("qagUUIDList") qagUUIDList: List<UUID>): List<QagUpdatesDTO>
}