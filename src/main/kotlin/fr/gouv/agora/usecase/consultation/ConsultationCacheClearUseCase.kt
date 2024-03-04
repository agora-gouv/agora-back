package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import org.springframework.stereotype.Service

@Service
class ConsultationCacheClearUseCase(
    private val consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository,
    private val consultationFinishedPaginatedRepository: ConsultationFinishedPaginatedListCacheRepository,
) {

    fun clearConsultationCaches() {
        consultationPreviewOngoingRepository.clearCache()
        consultationFinishedPaginatedRepository.clearCache()
    }

}