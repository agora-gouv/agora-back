package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConsultationDatabaseRepository: CrudRepository<ConsultationDTO, UUID> {
    @Query(value = "SELECT * FROM consultations WHERE id = ?1", nativeQuery = true)
    fun getConsultation(id: UUID): ConsultationDTO?
}
