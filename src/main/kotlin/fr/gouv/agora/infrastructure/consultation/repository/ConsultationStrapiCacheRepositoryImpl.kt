package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class ConsultationStrapiCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager")
    private val shortTermCacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationStrapiCacheRepository {

    companion object {
        const val ONGOING_CONSULTATIONS_CACHE_NAME = "strapiOngoingConsultations"
        const val FINISHED_CONSULTATIONS_CACHE_NAME = "strapiFinishedConsultations"

        val LIST_TYPE_REF = object : TypeReference<List<ConsultationStrapiDTO>>() {}
    }

    private val logger = LoggerFactory.getLogger(ConsultationStrapiCacheRepositoryImpl::class.java)

    override fun getOngoingConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>? {
        return getFromCache(ONGOING_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories))
    }

    override fun putOngoingConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>) {
        putInCache(ONGOING_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories), data)
    }

    override fun evictOngoingConsultations() {
        logger.info("[ConsultationStrapiCache] EVICT - $ONGOING_CONSULTATIONS_CACHE_NAME")
        shortTermCacheManager.getCache(ONGOING_CONSULTATIONS_CACHE_NAME)?.clear()
    }

    override fun getFinishedConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>? {
        return getFromCache(FINISHED_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories))
    }

    override fun putFinishedConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>) {
        putInCache(FINISHED_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories), data)
    }

    override fun evictFinishedConsultations() {
        logger.info("[ConsultationStrapiCache] EVICT - $FINISHED_CONSULTATIONS_CACHE_NAME")
        shortTermCacheManager.getCache(FINISHED_CONSULTATIONS_CACHE_NAME)?.clear()
    }

    private fun toTerritoryKey(territories: List<Territoire>): String {
        return territories.map { it.value }.sorted().joinToString(",").ifEmpty { "all" }
    }

    private fun getFromCache(cacheName: String, cacheKey: String): List<ConsultationStrapiDTO>? {
        return try {
            val cacheEntry = shortTermCacheManager.getCache(cacheName)?.get(cacheKey)
            if (cacheEntry == null) {
                logger.info("[ConsultationStrapiCache] CACHE MISS - {}[{}]", cacheName, cacheKey)
                return null
            }
            val result = objectMapper.convertValue(cacheEntry.get(), LIST_TYPE_REF)
            logger.info("[ConsultationStrapiCache] CACHE HIT - {}[{}] → {} consultations", cacheName, cacheKey, result.size)
            result
        } catch (e: Exception) {
            logger.warn("[ConsultationStrapiCache] CACHE READ ERROR - {}[{}]: {}", cacheName, cacheKey, e.message)
            null
        }
    }

    private fun putInCache(cacheName: String, cacheKey: String, data: List<ConsultationStrapiDTO>) {
        try {
            shortTermCacheManager.getCache(cacheName)?.put(cacheKey, data)
            logger.info("[ConsultationStrapiCache] CACHE WRITE - {}[{}] → {} consultations", cacheName, cacheKey, data.size)
        } catch (e: Exception) {
            logger.warn("[ConsultationStrapiCache] CACHE WRITE ERROR - {}[{}]: {}", cacheName, cacheKey, e.message)
        }
    }
}
