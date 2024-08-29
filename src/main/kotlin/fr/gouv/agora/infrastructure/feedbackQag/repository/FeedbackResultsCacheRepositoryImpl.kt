package fr.gouv.agora.infrastructure.feedbackQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.FeedbackResults
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheResult
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheResult.*
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class FeedbackResultsCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : FeedbackResultsCacheRepository {

    companion object {
        private const val FEEDBACK_RESULTS_CACHE = "feedbackResults"
    }

    override fun getFeedbackResults(qagId: String): FeedbackResultsCacheResult {
        return when (val cacheContent = getCache()?.get(qagId, String::class.java)) {
            null -> FeedbackResultsCacheNotInitialized
            else -> try {
                CachedFeedbackResults(objectMapper.readValue(cacheContent, FeedbackResults::class.java))
            } catch (e: Exception) {
                FeedbackResultsCacheNotInitialized
            }
        }
    }

    override fun initFeedbackResults(qagId: String, results: FeedbackResults) {
        getCache()?.put(qagId, objectMapper.writeValueAsString(results))
    }

    override fun evictFeedbackResults(qagId: String) {
        getCache()?.evict(qagId)
    }

    private fun getCache() = cacheManager.getCache(FEEDBACK_RESULTS_CACHE)

}
