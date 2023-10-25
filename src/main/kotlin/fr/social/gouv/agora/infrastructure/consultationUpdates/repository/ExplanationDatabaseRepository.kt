package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExplanationDatabaseRepository : JpaRepository<ExplanationDTO, UUID> {

    @Query(
        value = "SELECT * FROM explanations WHERE consultation_updates_id IN :consultationUpdatesIDs",
        nativeQuery = true
    )
    fun getExplanationUpdates(@Param("consultationUpdatesIDs") consultationUpdatesIDs: List<UUID>): List<ExplanationDTO>

    @Query(
        value = "SELECT * FROM explanations WHERE consultation_updates_id = :consultationUpdatesUUId",
        nativeQuery = true
    )
    fun getExplanationList(@Param("consultationUpdatesUUId") consultationUpdatesUUId: UUID): List<ExplanationDTO>

}