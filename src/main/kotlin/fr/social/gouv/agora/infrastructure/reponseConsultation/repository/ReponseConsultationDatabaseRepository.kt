package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReponseConsultationDatabaseRepository : CrudRepository<ReponseConsultationDTO, UUID> {
}

