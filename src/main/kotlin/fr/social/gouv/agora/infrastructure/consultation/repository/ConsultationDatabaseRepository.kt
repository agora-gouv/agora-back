package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationDatabaseRepository : CrudRepository<ConsultationDTO, UUID> {
    @Query(value = "SELECT * FROM consultations WHERE id = :consultationId LIMIT 1", nativeQuery = true)
    fun getConsultation(@Param("consultationId") consultationId: UUID): ConsultationDTO?

    @Query(
        value = "SELECT consultations.id, title, abstract, start_date, end_date, cover_url,question_count," +
                "estimated_time, participant_count_goal, consultations.description, tips_description, thematique_id " +
                "FROM consultations JOIN consultation_updates " +
                "ON (consultations.id = consultation_updates.consultation_id) WHERE consultation_updates.step = 1",
        nativeQuery = true
    )
    fun getConsultationOngoingList(): List<ConsultationDTO>?
}
