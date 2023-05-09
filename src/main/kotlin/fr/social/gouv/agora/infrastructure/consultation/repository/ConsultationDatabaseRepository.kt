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
        value = """SELECT * FROM consultations WHERE id NOT IN 
            (SELECT consultation_id FROM consultation_updates WHERE step = 2 OR step = 3)""",
        nativeQuery = true
    )
    fun getConsultationOngoingList(): List<ConsultationDTO>

    @Query(
        value = """SELECT * FROM consultations WHERE id IN 
            (SELECT DISTINCT consultation_id FROM reponses_consultation WHERE user_id = :userId LIMIT 10)""",
        nativeQuery = true
    )
    fun getConsultationAnsweredList(@Param("userId") userId: UUID): List<ConsultationDTO>

}
