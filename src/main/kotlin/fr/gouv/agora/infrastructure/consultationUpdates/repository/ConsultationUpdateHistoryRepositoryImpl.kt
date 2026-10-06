package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateHistoryRepositoryImpl(
    val consultationStrapiRepository: ConsultationStrapiRepository,
    val featureFlagsRepository: FeatureFlagsRepository,
    val mapper: ConsultationUpdateHistoryMapper,
    @Qualifier("shortTermCacheManager")
    val shortTermCacheManager: CacheManager,
) : ConsultationUpdateHistoryRepository {

    companion object {
        private const val CACHE_NAME = "consultationUpdateHistory"
    }

    private val logger = LoggerFactory.getLogger(ConsultationUpdateHistoryRepositoryImpl::class.java)

    override fun getConsultationUpdateHistory(consultationId: String): List<ConsultationUpdateHistory> {
        val cache = shortTermCacheManager.getCache(CACHE_NAME)

        try {
            @Suppress("UNCHECKED_CAST")
            val cached = cache?.get(consultationId)?.get() as? List<ConsultationUpdateHistory>
            if (cached != null) {
                logger.info("[ConsultationUpdateHistoryCache] HIT - consultationId={}", consultationId)
                return cached
            }
        } catch (e: Exception) {
            logger.warn("[ConsultationUpdateHistoryCache] READ ERROR - consultationId={}: {}", consultationId, e.message)
        }

        logger.info("[ConsultationUpdateHistoryCache] MISS - consultationId={}", consultationId)
        val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
            ?: return emptyList()

        val result = mapper.toDomain(consultation)

        try {
            cache?.put(consultationId, result)
            logger.info("[ConsultationUpdateHistoryCache] WRITE - consultationId={}", consultationId)
        } catch (e: Exception) {
            logger.warn("[ConsultationUpdateHistoryCache] WRITE ERROR - consultationId={}: {}", consultationId, e.message)
        }

        return result
    }
}
