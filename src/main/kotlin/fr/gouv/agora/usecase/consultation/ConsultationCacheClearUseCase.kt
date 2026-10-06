package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import org.springframework.stereotype.Service

@Service
class ConsultationCacheClearUseCase(
    private val consultationDetailsV2CacheRepository: ConsultationDetailsV2CacheRepository,
    private val consultationFinishedPaginatedRepository: ConsultationFinishedPaginatedListCacheRepository,
    private val consultationStrapiCacheRepository: ConsultationStrapiCacheRepository,
) {

    fun clearConsultationCaches() {
        consultationDetailsV2CacheRepository.clearLastConsultationDetails()
        consultationFinishedPaginatedRepository.clearCache()
        consultationStrapiCacheRepository.evictOngoingConsultations()
        consultationStrapiCacheRepository.evictFinishedConsultations()
    }

}
