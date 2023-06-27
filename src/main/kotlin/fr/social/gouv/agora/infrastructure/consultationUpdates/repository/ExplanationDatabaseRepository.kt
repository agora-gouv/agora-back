package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExplanationDatabaseRepository : CrudRepository<ExplanationDTO, UUID> {

    @Query(
        value = "SELECT * FROM explanations WHERE consultation_updates_id = :consultationUpdatesUUId",
        nativeQuery = true
    )
    fun getExplanationList(@Param("consultationUpdatesUUId") consultationUpdatesUUId: UUID): List<ExplanationDTO>

}