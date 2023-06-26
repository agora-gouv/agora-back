package fr.social.gouv.agora.infrastructure.qagUpdates.repository

import fr.social.gouv.agora.infrastructure.qagUpdates.dto.QagUpdatesDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QagUpdatesDatabaseRepository : CrudRepository<QagUpdatesDTO, UUID> {

    @Query(value = "SELECT * FROM qag_updates WHERE qag_id in :qagUUIDList", nativeQuery = true)
    fun getQagUpdates(@Param("qagUUIDList") qagUUIDList: List<UUID>): List<QagUpdatesDTO>
}