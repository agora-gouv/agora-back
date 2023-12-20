package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationParticipantCountUseCase(
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
) {
    fun getCount(consultationId: String): Int {
        return userAnsweredConsultationRepository.getParticipantCount(consultationId = consultationId)
    }
}