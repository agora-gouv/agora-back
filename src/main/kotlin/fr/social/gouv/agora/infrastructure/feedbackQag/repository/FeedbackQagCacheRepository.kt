package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FeedbackQagCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val FEEDBACK_QAG_CACHE_NAME = "feedbackQagCache"
        const val FEEDBACK_QAG_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    sealed class CacheResult {
        data class CachedFeedbackQag(val feedbackQagDTO: FeedbackQagDTO) : CacheResult()
        object CachedFeedbackQagNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun insertFeedbackQag(qagId: UUID, userId: String, feedbackQagDTO: FeedbackQagDTO?) {
        getCache()?.put(
            buildFeedbackQagCacheKey(qagId, userId),
            feedbackQagDTO ?: createFeedbackQagNotFound(),
        )
    }

    fun getFeedbackQag(qagId: UUID, userId: String): CacheResult {
        val feedbackQagDTO = try {
            getCache()?.get(buildFeedbackQagCacheKey(qagId, userId), FeedbackQagDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (feedbackQagDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            FEEDBACK_QAG_NOT_FOUND_ID -> CacheResult.CachedFeedbackQagNotFound
            else -> CacheResult.CachedFeedbackQag(feedbackQagDTO)
        }
    }

    private fun getCache() = cacheManager.getCache(FEEDBACK_QAG_CACHE_NAME)
    private fun buildFeedbackQagCacheKey(qagId: UUID, userId: String) = "$qagId/$userId"

    private fun createFeedbackQagNotFound() = FeedbackQagDTO(
        id = UUID.fromString(FEEDBACK_QAG_NOT_FOUND_ID),
        userId = "",
        qagId = UUID.fromString(FEEDBACK_QAG_NOT_FOUND_ID),
        isHelpful = false,
    )

}