package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheRepository
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

    override fun getOngoingConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>? {
        return getFromCache(ONGOING_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories))
    }

    override fun putOngoingConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>) {
        putInCache(ONGOING_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories), data)
    }

    override fun evictOngoingConsultations() {
        shortTermCacheManager.getCache(ONGOING_CONSULTATIONS_CACHE_NAME)?.clear()
    }

    override fun getFinishedConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>? {
        return getFromCache(FINISHED_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories))
    }

    override fun putFinishedConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>) {
        putInCache(FINISHED_CONSULTATIONS_CACHE_NAME, toTerritoryKey(territories), data)
    }

    override fun evictFinishedConsultations() {
        shortTermCacheManager.getCache(FINISHED_CONSULTATIONS_CACHE_NAME)?.clear()
    }

    private fun toTerritoryKey(territories: List<Territoire>): String {
        return territories.map { it.value }.sorted().joinToString(",").ifEmpty { "all" }
    }

    private fun getFromCache(cacheName: String, cacheKey: String): List<ConsultationStrapiDTO>? {
        return try {
            val cachedValue = shortTermCacheManager.getCache(cacheName)
                ?.get(cacheKey, String::class.java)
                ?: return null
            objectMapper.readValue(cachedValue, LIST_TYPE_REF)
        } catch (e: Exception) {
            null
        }
    }

    private fun putInCache(cacheName: String, cacheKey: String, data: List<ConsultationStrapiDTO>) {
        try {
            shortTermCacheManager.getCache(cacheName)
                ?.put(cacheKey, objectMapper.writeValueAsString(data))
        } catch (e: Exception) {
            // Ne pas planter si le cache échoue
        }
    }
}
