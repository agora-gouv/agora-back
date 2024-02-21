package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryDTO
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryWithDateDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationUpdateHistoryDatabaseRepository: JpaRepository<ConsultationUpdateHistoryDTO, UUID> {

    @Query(
        value = """SELECT step_number AS stepNumber, type, consultation_update_id AS consultationUpdateId, title, update_date AS updateDate, action_text AS actionText
            FROM consultation_update_history LEFT JOIN consultation_updates_v2
            ON consultation_update_history.consultation_update_id = consultation_updates_v2.id
            WHERE consultation_update_history.consultation_id = :consultationId
            ORDER BY stepNumber ASC, updateDate ASC""",
        nativeQuery = true
    )
    fun getConsultationUpdateHistory(@Param("consultationId") consultationId: UUID): List<ConsultationUpdateHistoryWithDateDTO>

}