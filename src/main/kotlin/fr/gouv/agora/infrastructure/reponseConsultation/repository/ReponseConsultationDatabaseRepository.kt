package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ResponseConsultationCountDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReponseConsultationDatabaseRepository : JpaRepository<ReponseConsultationDTO, UUID> {

    @Query(value = "SELECT * FROM reponses_consultation WHERE consultation_id = :consultationId", nativeQuery = true)
    fun getConsultationResponses(@Param("consultationId") consultationId: UUID): List<ReponseConsultationDTO>

    @Query(
        value = """SELECT count(*)
        FROM (SELECT DISTINCT participation_id
            FROM reponses_consultation 
            WHERE consultation_id = :consultationId
        ) as participationIds
        """, nativeQuery = true
    )
    fun getParticipantCount(@Param("consultationId") consultationId: UUID): Int

    @Query(
        value = """SELECT question_id as questionId, choice_id as choiceId, count(*) as responseCount
            FROM reponses_consultation
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (question_id, choice_id)
            """,
        nativeQuery = true,
    )
    fun getConsultationResponsesCount(@Param("consultationId") consultationId: UUID): List<ResponseConsultationCountDTO>

    @Query(
        value = """SELECT DISTINCT consultation_id FROM reponses_consultation
            WHERE consultation_id IN :consultationIDs
            AND user_id = :userId
        """,
        nativeQuery = true,
    )
    fun getAnsweredConsultations(
        @Param("consultationIDs") consultationIDs: List<UUID>,
        @Param("userId") userId: UUID
    ): List<UUID>

    @Query(
        value = "SELECT DISTINCT user_id from reponses_consultation WHERE consultation_id = :consultationId",
        nativeQuery = true
    )
    fun getUsersAnsweredConsultation(@Param("consultationId") consultationId: UUID): List<UUID>

}