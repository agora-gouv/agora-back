package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface ReponseConsultationDatabaseRepository : CrudRepository<ReponseConsultationDTO, UUID> {

    @Query(value = "SELECT * FROM reponses_consultation WHERE consultation_id = :consultationId", nativeQuery = true)
    fun getConsultationResponses(@Param("consultationId") consultationId: UUID): List<ReponseConsultationDTO>

}