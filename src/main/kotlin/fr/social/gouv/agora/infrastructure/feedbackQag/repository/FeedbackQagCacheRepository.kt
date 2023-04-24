package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FeedbackQagCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val FEEDBACK_QAG_CACHE_NAME = "feedbackQagCache"
    }

    fun insertFeedbackQag(qagId: UUID, userId: String, feedbackQagDTO: FeedbackQagDTO) {
        getCache()?.put(buildSupportQagCacheKey(qagId, userId), feedbackQagDTO)
    }

    private fun getCache() = cacheManager.getCache(FEEDBACK_QAG_CACHE_NAME)
    private fun buildSupportQagCacheKey(qagId: UUID, userId: String) = "$qagId/$userId"
}