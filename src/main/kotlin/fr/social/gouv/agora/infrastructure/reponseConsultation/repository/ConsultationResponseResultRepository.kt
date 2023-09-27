package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.infrastructure.reponseConsultation.ConsultationResultJson
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
) : ConsultationResponseResultJsonCacheRepository {

    companion object {
        private const val CONSULTATION_RESPONSE_RESULT_JSON_CACHE_NAME = "consultationResult"
    }

    override fun getConsultationResults(consultationId: String): ConsultationResultJson? {
        return getCache()?.get(consultationId, ConsultationResultJson::class.java)
    }

    override fun insertConsultationResults(consultationId: String, results: ConsultationResultJson) {
        getCache()?.put(consultationId, results)
    }

    override fun evictConsultationResults(consultationId: String) {
        getCache()?.evict(consultationId)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_RESPONSE_RESULT_JSON_CACHE_NAME)

}