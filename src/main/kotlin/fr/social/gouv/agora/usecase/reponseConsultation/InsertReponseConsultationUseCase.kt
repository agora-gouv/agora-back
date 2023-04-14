package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.InsertStatus
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ReponseConsultationRepository
import org.springframework.stereotype.Service

@Service
class InsertReponseConsultationUseCase(private val repository: ReponseConsultationRepository) {
    fun insertReponseConsultation(reponseConsultation: ReponseConsultationInserting): InsertStatus {
        return repository.insertReponseConsultation(reponseConsultation)
    }
}