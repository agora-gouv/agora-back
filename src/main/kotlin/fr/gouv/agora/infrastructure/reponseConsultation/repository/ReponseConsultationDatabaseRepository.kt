package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.infrastructure.reponseConsultation.dto.*
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
        value = """SELECT choice_id as choiceId, gender as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, gender)
            """,
        nativeQuery = true,
    )
    fun getConsultationGenderByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, year_of_birth as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, year_of_birth)
            """,
        nativeQuery = true,
    )
    fun getConsultationYearOfBirthByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, department as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, department)
            """,
        nativeQuery = true,
    )
    fun getConsultationDepartmentByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, city_type as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, city_type)
            """,
        nativeQuery = true,
    )
    fun getConsultationCityTypeByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, job_category as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, job_category)
            """,
        nativeQuery = true,
    )
    fun getConsultationJobCategoryByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, vote_frequency as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, vote_frequency)
            """,
        nativeQuery = true,
    )
    fun getConsultationVoteFrequencyByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, public_meeting_frequency as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, public_meeting_frequency)
            """,
        nativeQuery = true,
    )
    fun getConsultationPublicMeetingFrequencyByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

    @Query(
        value = """SELECT choice_id as choiceId, consultation_frequency as key, count(DISTINCT users_profile.user_id) AS count
            FROM reponses_consultation LEFT JOIN users_profile
            ON reponses_consultation.user_id = users_profile.user_id
            WHERE consultation_id = :consultationId
            AND choice_id IS NOT NULL
            GROUP BY (choice_id, consultation_frequency)
            """,
        nativeQuery = true,
    )
    fun getConsultationConsultationFrequencyByChoice(@Param("consultationId") consultationId: UUID): List<DemographicInfoCountByChoiceDTO>

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