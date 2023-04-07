package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationUseCase(private val repository: ConsultationRepository) {
    fun getConsultation(id: String): Consultation? {
        return repository.getConsultation(id)
    }
}