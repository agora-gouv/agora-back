package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.infrastructure.qag.dto.LowPriorityQagDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LowPriorityQagDatabaseRepository : JpaRepository<LowPriorityQagDTO, UUID> {

    @Query(value = "SELECT qag_id FROM low_priority_qags WHERE qag_id IN :qagIds", nativeQuery = true)
    fun getLowPriorityQagIds(@Param("qagIds") qagIds: List<UUID>): List<UUID>

}