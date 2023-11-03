package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.infrastructure.reponseConsultation.dto.DemographicInfoCountDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ResponseConsultationCountDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ResponseConsultationCountWithDemographicInfoDTO
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
        value = """SELECT gender as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY gender
            """,
        nativeQuery = true,
    )
    fun getConsultationGender(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT year_of_birth as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY year_of_birth
            """,
        nativeQuery = true,
    )
    fun getConsultationYearOfBirth(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT department as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY department
            """,
        nativeQuery = true,
    )
    fun getConsultationDepartment(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT city_type as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY city_type
            """,
        nativeQuery = true,
    )
    fun getConsultationCityType(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT job_category as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY job_category
            """,
        nativeQuery = true,
    )
    fun getConsultationJobCategory(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT vote_frequency as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY vote_frequency
            """,
        nativeQuery = true,
    )
    fun getConsultationVoteFrequency(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT public_meeting_frequency as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY public_meeting_frequency
            """,
        nativeQuery = true,
    )
    fun getConsultationPublicMeetingFrequency(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT consultation_frequency as key, count(DISTINCT user_id) as count
            FROM users_profile
            WHERE user_id IN (
                SELECT DISTINCT user_id
                FROM reponses_consultation
                WHERE consultation_id = :consultationId
            )
            GROUP BY consultation_frequency
            """,
        nativeQuery = true,
    )
    fun getConsultationConsultationFrequency(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountDTO>

    @Query(
        value = """SELECT question_id as questionId, choice_id as choiceId, count(*) AS responseCount, gender, year_of_birth as yearOfBirth, department, city_type as cityType, job_category as jobCategory, vote_frequency as voteFrequency, public_meeting_frequency as publicMeetingFrequency, consultation_frequency as consultationFrequency
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (question_id, choice_id, gender, year_of_birth, department, city_type, job_category, vote_frequency, public_meeting_frequency, consultation_frequency)
            """,
        nativeQuery = true,
    )
    fun getConsultationResponsesCountWithDemographicInfo(@Param("consultationId") consultationId: UUID): List<ResponseConsultationCountWithDemographicInfoDTO>

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