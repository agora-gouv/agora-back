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