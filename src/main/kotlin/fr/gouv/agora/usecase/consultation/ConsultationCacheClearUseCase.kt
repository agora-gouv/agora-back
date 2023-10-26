package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
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