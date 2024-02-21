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
        value = """SELECT update_label FROM consultation_updates 
            WHERE consultation_id = :consultationId
            AND CURRENT_TIMESTAMP > update_date
            ORDER BY update_date
            LIMIT 1""",
        nativeQuery = true
    )
    fun getLastConsultationUpdateLabel(@Param("consultationId") consultationId: UUID): String

    @Query(
        value = """SELECT * FROM consultation_updates 
            WHERE consultation_id = :consultationId
            AND CURRENT_TIMESTAMP > update_date
            ORDER BY update_date
            LIMIT 1""",
        nativeQuery = true
    )
    fun getLastConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateV2DTO

    @Query(
        value = """SELECT * FROM consultation_updates 
            WHERE consultation_id = :consultationId
            AND CURRENT_TIMESTAMP > update_date
            ORDER BY update_date
            LIMIT 1""",
        nativeQuery = true
    )
    fun getConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateV2DTO?

}