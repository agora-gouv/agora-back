package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetReponseConsultationRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationParticipantCountUseCase(
    private val getReponseConsultationRepository: GetReponseConsultationRepository,
) {
    fun getCount(consultationId: String): Int {
        return getReponseConsultationRepository.getConsultationResponses(consultationId).map { it.participationId }
            .toSet().size
    }
}