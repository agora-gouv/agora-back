package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationDatabaseRepository : CrudRepository<ConsultationDTO, UUID> {

    // TODO: optimize query when consultations table will grow bigger
    @Query(value = "SELECT * FROM consultations ORDER BY start_date DESC", nativeQuery = true)
    fun getConsultations(): List<ConsultationDTO>

    @Query(value = "SELECT * FROM consultations WHERE id = :consultationId LIMIT 1", nativeQuery = true)
    fun getConsultation(@Param("consultationId") consultationId: UUID): ConsultationDTO?

    @Query(
        value = """SELECT * FROM consultations 
            WHERE CURRENT_DATE < end_date
            AND (start_date IS NULL OR CURRENT_DATE >= start_date) 
            ORDER BY end_date ASC""",
        nativeQuery = true
    )
    fun getConsultationOngoingList(): List<ConsultationDTO>

    @Query(
        value = """SELECT * FROM consultations 
            WHERE CURRENT_DATE >= end_date
            ORDER BY end_date ASC""",
        nativeQuery = true
    )
    fun getConsultationFinishedList(): List<ConsultationDTO>

    @Query(
        value = """SELECT * FROM consultations WHERE id IN 
            (SELECT DISTINCT consultation_id FROM reponses_consultation WHERE user_id = :userId LIMIT 10)""",
        nativeQuery = true
    )
    fun getConsultationAnsweredList(@Param("userId") userId: UUID): List<ConsultationDTO>

}
