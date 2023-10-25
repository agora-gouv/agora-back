package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.domain.FeedbackResults
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheResult
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheResult.*
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
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