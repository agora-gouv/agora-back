package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationUpdateDatabaseRepository : JpaRepository<ConsultationUpdateDTO, UUID> {

    @Query(value = "SELECT * FROM consultation_updates WHERE consultation_id IN :consultationIDs", nativeQuery = true)
    fun getConsultationUpdates(@Param("consultationIDs") consultationIDs: List<UUID>): List<ConsultationUpdateDTO>

    @Query(
        value = "SELECT * FROM consultation_updates WHERE consultation_id = :consultationId AND step = 1 LIMIT 1",
        nativeQuery = true
    )
    fun getOngoingConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateDTO?

    @Query(
        value = "SELECT * FROM consultation_updates WHERE consultation_id = :consultationId AND step > 1 ORDER BY step DESC LIMIT 1",
        nativeQuery = true
    )
    fun getFinishedConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateDTO?

}