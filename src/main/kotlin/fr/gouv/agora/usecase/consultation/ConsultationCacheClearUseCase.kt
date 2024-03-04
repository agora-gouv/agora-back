package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import org.springframework.stereotype.Service

@Service
class ConsultationCacheClearUseCase(
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
    private val consultationDetailsV2CacheRepository: ConsultationDetailsV2CacheRepository,
    private val consultationFinishedPaginatedRepository: ConsultationFinishedPaginatedListCacheRepository,
) {

    fun clearConsultationCaches() {
        consultationPreviewPageRepository.clear()
        consultationDetailsV2CacheRepository.clearLastConsultationDetails()
        consultationFinishedPaginatedRepository.clearCache()
    }

}