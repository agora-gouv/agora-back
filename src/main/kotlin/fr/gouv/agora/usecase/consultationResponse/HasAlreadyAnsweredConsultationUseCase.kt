package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import org.springframework.stereotype.Service

@Service
class HasAlreadyAnsweredConsultationUseCase(
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
) {
    fun hasAlreadyAnswered(consultationId: String, userId: String): Boolean =
        userAnsweredConsultationRepository.hasAnsweredConsultation(
            consultationId = consultationId,
            userId = userId,
        )
}
