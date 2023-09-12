package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FeedbackQagCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val FEEDBACK_QAG_CACHE_NAME = "feedbackQagCache"
    }

    sealed class CacheResult {
        data class CachedFeedbackQag(val feedbackQagDTO: FeedbackQagDTO) : CacheResult()
        object CachedFeedbackQagNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun insertFeedbackQag(qagId: UUID, userId: UUID, feedbackQagDTO: FeedbackQagDTO?) {
        getCache()?.put(
            buildFeedbackQagCacheKey(qagId, userId),
            feedbackQagDTO ?: createFeedbackQagNotFound(),
        )
    }

    fun getFeedbackQag(qagId: UUID, userId: UUID): CacheResult {
        val feedbackQagDTO = try {
            getCache()?.get(buildFeedbackQagCacheKey(qagId, userId), FeedbackQagDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (feedbackQagDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            UuidUtils.NOT_FOUND_UUID_STRING -> CacheResult.CachedFeedbackQagNotFound
            else -> CacheResult.CachedFeedbackQag(feedbackQagDTO)
        }
    }

    private fun getCache() = cacheManager.getCache(FEEDBACK_QAG_CACHE_NAME)
    private fun buildFeedbackQagCacheKey(qagId: UUID, userId: UUID) = "$qagId/$userId"

    private fun createFeedbackQagNotFound() = FeedbackQagDTO(
        id = UuidUtils.NOT_FOUND_UUID,
        userId = UuidUtils.NOT_FOUND_UUID,
        qagId = UuidUtils.NOT_FOUND_UUID,
        isHelpful = 0,
    )

}