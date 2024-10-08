package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationWithUpdateInfoDatabaseDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface ConsultationDatabaseRepository : JpaRepository<ConsultationDTO, UUID> {

    companion object {
        private const val CONSULTATION_WITH_UPDATE_INFO_PROJECTION =
            "id, slug, title, coverUrl, thematiqueId, endDate, updateDate, updateLabel"

        private const val CONSULTATION_WITH_UPDATE_INFO_JOIN = """
             SELECT consultations.id AS id, consultations.slug as slug, title, cover_url AS coverUrl, thematique_id AS thematiqueId, end_date AS endDate, update_date AS updateDate, update_label AS updateLabel, ROW_NUMBER() OVER (PARTITION BY consultations.id ORDER BY update_date DESC) AS consultationRowNumber
                FROM consultations LEFT JOIN consultation_updates_v2
                ON consultation_updates_v2.consultation_id = consultations.id
        """
    }

    @Query(
        value = """
        SELECT * FROM consultations
        WHERE :today > end_date + INTERVAL '14 DAYS'""", nativeQuery = true
    )
    fun getConsultationsEnded14DaysAgo(@Param("today") today: LocalDateTime): List<ConsultationDTO>

    @Query(value = "SELECT * FROM consultations WHERE id = :consultationId LIMIT 1", nativeQuery = true)
    fun getConsultation(@Param("consultationId") consultationId: UUID): ConsultationDTO?

    @Query(
        value = """
            SELECT * FROM consultations 
            WHERE slug = :consultationIdOrSlug 
            OR CAST(id as TEXT) = :consultationIdOrSlug LIMIT 1""", nativeQuery = true
    )
    fun getConsultationByIdOrSlug(@Param("consultationIdOrSlug") consultationIdOrSlug: String): ConsultationDTO?

    @Query(
        value = """SELECT * FROM consultations 
            WHERE :today < end_date
            AND (start_date IS NULL OR :today >= start_date) 
            ORDER BY end_date ASC""",
        nativeQuery = true
    )
    fun getConsultationOngoingList(@Param("today") today: LocalDateTime): List<ConsultationDTO>

    @Query(
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                WHERE :today > end_date
                AND :today >= update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            LIMIT 7
        """,
        nativeQuery = true
    )
    fun getConsultationsFinishedPreviewWithUpdateInfo(@Param("today") today: LocalDateTime): List<ConsultationWithUpdateInfoDatabaseDTO>

    @Query(
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                WHERE CURRENT_DATE > update_date
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            AND id IN :consultationsId
            ORDER BY updateDate DESC
        """,
        nativeQuery = true
    )
    fun getConsultationsPreviewWithUpdateInfo(@Param("consultationsId") consultationsId: List<UUID>): List<ConsultationWithUpdateInfoDatabaseDTO>

    @Query(
        value = """SELECT COUNT(*)
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                WHERE CURRENT_DATE > end_date
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
        """,
        nativeQuery = true
    )
    fun getConsultationFinishedCount(): Int

    @Query(
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                WHERE CURRENT_DATE > end_date
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            OFFSET :offset
            LIMIT :pageSize
        """,
        nativeQuery = true
    )
    fun getConsultationsFinishedWithUpdateInfo(@Param("offset") offset: Int, @Param("pageSize") pageSize: Int): List<ConsultationWithUpdateInfoDatabaseDTO>

    @Query(
        value = """SELECT COUNT(*)
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            AND CAST(id as TEXT) IN (SELECT consultation_id FROM user_answered_consultation WHERE user_id = :userId)
        """,
        nativeQuery = true
    )
    fun getConsultationAnsweredCount(@Param("userId") userId: UUID): Int

    @Query(
        value = """
            SELECT consultation_id FROM user_answered_consultation WHERE user_id = :userId
        """,
        nativeQuery = true
    )
    fun getConsultationAnsweredIds(@Param("userId") userId: UUID): List<String>

    @Query(
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            AND CAST(id as TEXT) IN (SELECT consultation_id FROM user_answered_consultation WHERE user_id = :userId)
            OFFSET :offset
            LIMIT 20
        """,
        nativeQuery = true
    )
    fun getConsultationsAnsweredWithUpdateInfo(
        @Param("userId") userId: UUID,
        @Param("offset") offset: Int,
    ): List<ConsultationWithUpdateInfoDatabaseDTO>
}
