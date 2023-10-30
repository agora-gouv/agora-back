package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import org.springframework.stereotype.Service

@Service
class ConsultationCacheClearUseCase(
    private val consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository,
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
) {

    fun clearConsultationCaches() {
        consultationPreviewOngoingRepository.clearCache()
        consultationPreviewFinishedRepository.clearCache()
    }

}