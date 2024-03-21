package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationWithUpdateInfoDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationDatabaseRepository : JpaRepository<ConsultationDTO, UUID> {

    companion object {
        private const val CONSULTATION_WITH_UPDATE_INFO_PROJECTION =
            "id, title, coverUrl, thematiqueId, endDate, updateDate, updateLabel"

        private const val CONSULTATION_WITH_UPDATE_INFO_JOIN = """
             SELECT consultations.id AS id, title, cover_url AS coverUrl, thematique_id AS thematiqueId, end_date AS endDate, update_date AS updateDate, update_label AS updateLabel, ROW_NUMBER() OVER (PARTITION BY consultations.id ORDER BY update_date DESC) AS consultationRowNumber
                FROM consultations LEFT JOIN consultation_updates_v2
                ON consultation_updates_v2.consultation_id = consultations.id
        """
    }

    @Query(
        value = """
        SELECT * FROM consultations
        WHERE CURRENT_TIMESTAMP > end_date + INTERVAL '14 DAYS'""", nativeQuery = true
    )
    fun getConsultationsToAggregate(): List<ConsultationDTO>

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
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                SELECT consultations.id AS id, title, cover_url AS coverUrl, thematique_id AS thematiqueId, end_date AS endDate, update_date AS updateDate, update_label AS updateLabel, ROW_NUMBER() OVER (PARTITION BY consultations.id ORDER BY update_date DESC) AS consultationRowNumber
                FROM consultations LEFT JOIN consultation_updates_v2
                ON consultation_updates_v2.consultation_id = consultations.id
                WHERE CURRENT_DATE > end_date
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            LIMIT 5
        """,
        nativeQuery = true
    )
    fun getConsultationsFinishedPreviewWithUpdateInfo(): List<ConsultationWithUpdateInfoDTO>

    @Query(
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                WHERE CURRENT_DATE > update_date
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            AND id IN (SELECT consultation_id FROM user_answered_consultation WHERE user_id = :userId)
            ORDER BY updateDate DESC
        """,
        nativeQuery = true
    )
    fun getConsultationsAnsweredPreviewWithUpdateInfo(@Param("userId") userId: UUID): List<ConsultationWithUpdateInfoDTO>

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
            LIMIT 20
        """,
        nativeQuery = true
    )
    fun getConsultationsFinishedWithUpdateInfo(@Param("offset") offset: Int): List<ConsultationWithUpdateInfoDTO>

    @Query(
        value = """SELECT COUNT(*)
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            AND id IN (SELECT consultation_id FROM user_answered_consultation WHERE user_id = :userId)
        """,
        nativeQuery = true
    )
    fun getConsultationAnsweredCount(@Param("userId") userId: UUID): Int

    @Query(
        value = """SELECT $CONSULTATION_WITH_UPDATE_INFO_PROJECTION
            FROM (
                $CONSULTATION_WITH_UPDATE_INFO_JOIN
                AND CURRENT_DATE > update_date
                ORDER BY updateDate DESC
            ) as consultationAndUpdates
            WHERE consultationRowNumber = 1
            AND id IN (SELECT consultation_id FROM user_answered_consultation WHERE user_id = :userId)
            OFFSET :offset
            LIMIT 20
        """,
        nativeQuery = true
    )
    fun getConsultationsAnsweredWithUpdateInfo(
        @Param("userId") userId: UUID,
        @Param("offset") offset: Int,
    ): List<ConsultationWithUpdateInfoDTO>

}
