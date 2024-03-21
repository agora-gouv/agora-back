package fr.gouv.agora.infrastructure.consultationResponse.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.infrastructure.consultationResults.ConsultationResultsWithUpdateJson
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

interface ConsultationResponseResultJsonCacheRepository {
    fun getConsultationResults(consultationId: String): ConsultationResultsWithUpdateJson?
    fun insertConsultationResults(consultationId: String, results: ConsultationResultsWithUpdateJson)
    fun evictConsultationResults(consultationId: String)
}

@Component
@Suppress("unused")
class ConsultationResponseResultJsonRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationResponseResultJsonCacheRepository {

    companion object {
        private const val CONSULTATION_RESPONSE_RESULT_JSON_CACHE_NAME = "consultationResult"
    }

    override fun getConsultationResults(consultationId: String): ConsultationResultsWithUpdateJson? {
        return getCache()?.get(consultationId, String::class.java)
            ?.let { cacheContent ->
                try {
                    objectMapper.readValue(cacheContent, ConsultationResultsWithUpdateJson::class.java)
                } catch (e: Exception) {
                    null
                }
            }
    }

    override fun insertConsultationResults(consultationId: String, results: ConsultationResultsWithUpdateJson) {
        getCache()?.put(consultationId, objectMapper.writeValueAsString(results))
    }

    override fun evictConsultationResults(consultationId: String) {
        getCache()?.evict(consultationId)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_RESPONSE_RESULT_JSON_CACHE_NAME)
}