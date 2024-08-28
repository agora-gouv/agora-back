package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateV2DTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationUpdateInfoV2DatabaseRepository : JpaRepository<ConsultationUpdateV2DTO, UUID> {

    @Query(
        value = """SELECT * FROM consultation_updates_v2
            WHERE consultation_id = :consultationId
            AND CURRENT_TIMESTAMP > update_date
            AND is_visible_to_unanswered_users_only = 1
            ORDER BY update_date DESC
            LIMIT 1""",
        nativeQuery = true
    )
    fun getUnansweredUsersConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateV2DTO

    @Query(
        value = """SELECT * FROM consultation_updates_v2
            WHERE consultation_id = :consultationId
            AND CURRENT_TIMESTAMP > update_date
            AND is_visible_to_unanswered_users_only = 0
            ORDER BY update_date DESC
            LIMIT 1""",
        nativeQuery = true
    )
    fun getLatestConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateV2DTO

    @Query(
        value = """SELECT * FROM consultation_updates_v2
            WHERE consultation_id = :consultationId
            AND id = :consultationUpdateId
            AND CURRENT_TIMESTAMP > update_date
            LIMIT 1""",
        nativeQuery = true
    )
    fun getConsultationUpdate(@Param("consultationId") consultationId: UUID, @Param("consultationUpdateId") consultationUpdateId: UUID): ConsultationUpdateV2DTO?

    @Query(
        value = """SELECT * FROM consultation_updates_v2
            WHERE CAST(consultation_id as TEXT) = :consultationId
            AND (slug = :consultationUpdateIdOrSlug OR CAST(id as TEXT) = :consultationUpdateIdOrSlug)
            AND CURRENT_TIMESTAMP > update_date
            LIMIT 1""",
        nativeQuery = true
    )
    fun getConsultationUpdateByIdOrSlug(@Param("consultationId") consultationId: String, @Param("consultationUpdateIdOrSlug") consultationUpdateIdOrSlug: String): ConsultationUpdateV2DTO?
}
