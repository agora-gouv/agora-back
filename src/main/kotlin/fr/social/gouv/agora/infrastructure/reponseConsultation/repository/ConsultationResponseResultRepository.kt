package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.infrastructure.reponseConsultation.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

interface ConsultationResponseResultJsonCacheRepository {
    fun getConsultationResults(consultationId: String): ConsultationResultJson?
    fun insertConsultationResults(consultationId: String, results: ConsultationResultJson)
    fun evictConsultationResults(consultationId: String)
}

@Component
@Suppress("unused")
class ConsultationResponseResultJsonRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationResponseResultJsonCacheRepository {

    companion object {
        private const val CONSULTATION_RESPONSE_RESULT_JSON_CACHE_NAME = "consultationResult"
    }

    override fun getConsultationResults(consultationId: String): ConsultationResultJson? {
        return getCache()?.get(consultationId, String::class.java)
            ?.let { objectMapper.readValue(it, ConsultationResultJson::class.java) }
    }

    override fun insertConsultationResults(consultationId: String, results: ConsultationResultJson) {
        getCache()?.put(consultationId, objectMapper.writeValueAsString(results))
    }

    override fun evictConsultationResults(consultationId: String) {
        getCache()?.evict(consultationId)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_RESPONSE_RESULT_JSON_CACHE_NAME)
}