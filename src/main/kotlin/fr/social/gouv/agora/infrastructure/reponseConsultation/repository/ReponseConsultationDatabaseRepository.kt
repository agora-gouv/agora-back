package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReponseConsultationDatabaseRepository : CrudRepository<ReponseConsultationDTO, UUID> {

    @Query(value = "SELECT * FROM reponses_consultation WHERE consultation_id = :consultationId", nativeQuery = true)
    fun getConsultationResponses(@Param("consultationId") consultationId: UUID): List<ReponseConsultationDTO>

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

}