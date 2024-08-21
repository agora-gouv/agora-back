package fr.gouv.agora.infrastructure.userAnsweredConsultation.repository

import fr.gouv.agora.infrastructure.userAnsweredConsultation.dto.UserAnsweredConsultationDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserAnsweredConsultationDatabaseRepository : JpaRepository<UserAnsweredConsultationDTO, UUID> {

    @Query(
        value = "SELECT count(DISTINCT user_id) FROM user_answered_consultation WHERE consultation_id = :consultationId",
        nativeQuery = true
    )
    fun getParticipantCount(@Param("consultationId") consultationId: String): Int

    @Query(
        value = """SELECT DISTINCT consultation_id FROM user_answered_consultation
            WHERE user_id = :userId
        """,
        nativeQuery = true,
    )
    fun getAnsweredConsultationIds(@Param("userId") userId: UUID): List<String>

    @Query(
        value = """SELECT count(DISTINCT user_id) FROM user_answered_consultation
            WHERE consultation_id = :consultationId
            AND user_id = :userId
        """,
        nativeQuery = true,
    )
    fun hasAnsweredConsultation(
        @Param("consultationId") consultationId: String,
        @Param("userId") userId: UUID,
    ): Int

    @Query(
        value = """SELECT DISTINCT consultation_id FROM user_answered_consultation
            WHERE consultation_id IN :consultationIds
            AND user_id = :userId
        """,
        nativeQuery = true,
    )
    fun getAnsweredConsultations(
        @Param("consultationIds") consultationIds: List<String>,
        @Param("userId") userId: UUID,
    ): List<String>

    @Query(
        value = "SELECT DISTINCT user_id FROM user_answered_consultation WHERE consultation_id = :consultationId",
        nativeQuery = true
    )
    fun getUsersAnsweredConsultation(@Param("consultationId") consultationId: String): List<UUID>

}
