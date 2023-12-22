package fr.gouv.agora.infrastructure.consultationResult.repository

import fr.gouv.agora.infrastructure.consultationResult.dto.ConsultationResultAggregatedDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationResultAggregatedDatabaseRepository : JpaRepository<ConsultationResultAggregatedDTO, UUID> {

    @Query(value = "SELECT * FROM consultation_results WHERE consultation_id = :consultationId", nativeQuery = true)
    fun getConsultationResultByConsultation(@Param("consultationId") consultationId: UUID): List<ConsultationResultAggregatedDTO>
}