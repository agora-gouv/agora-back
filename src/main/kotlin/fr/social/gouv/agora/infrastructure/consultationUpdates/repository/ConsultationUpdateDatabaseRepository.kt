package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationUpdateDatabaseRepository : CrudRepository<ConsultationUpdateDTO, UUID> {

    @Query(
        value = "SELECT * FROM consultation_updates WHERE consultation_id = :consultationId ORDER BY step DESC LIMIT 1",
        nativeQuery = true
    )
    fun getLastConsultationUpdate(@Param("consultationId") consultationId: UUID): ConsultationUpdateDTO?

}