package fr.gouv.agora.infrastructure.consultationResults.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheResult
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationResultsCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationResultsCacheRepository {

    companion object {
        private const val CONSULTATION_RESULTS_CACHE_NAME = "consultationResults"
    }

    override fun getConsultationResults(consultationId: String): ConsultationResultsCacheResult {
        return try {
            when (val cacheValue = getCache()?.get(consultationId, String::class.java)) {
                null -> ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized
                "" -> ConsultationResultsCacheResult.ConsultationResultsNotFound
                else -> ConsultationResultsCacheResult.CachedConsultationResults(
                    results = objectMapper.readValue(cacheValue, ConsultationResults::class.java)
                )
            }
        } catch (e: Exception) {
            ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized
        }
    }

    override fun initConsultationResults(consultationId: String, results: ConsultationResults) {
        getCache()?.put(consultationId, objectMapper.writeValueAsString(results))
    }

    override fun initConsultationResultsNotFound(consultationId: String) {
        getCache()?.put(consultationId, "")
    }

    override fun evictConsultationResultsCache(consultationId: String) {
        getCache()?.evict(consultationId)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_RESULTS_CACHE_NAME)

}