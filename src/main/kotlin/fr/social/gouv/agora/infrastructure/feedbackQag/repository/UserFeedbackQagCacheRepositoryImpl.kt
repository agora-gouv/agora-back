package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheResult
import fr.social.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class UserFeedbackQagCacheRepositoryImpl(
    private val cacheManager: CacheManager,
) : UserFeedbackQagCacheRepository {

    companion object {
        private const val USER_FEEDBACK_QAG_CACHE = "userFeedbackQags"
    }

    override fun getUserFeedbackQagIds(userId: String): UserFeedbackQagCacheResult {
        return when (val userFeedbackQagIds = getCacheContent(userId = userId)) {
            null -> UserFeedbackQagCacheResult.UserFeedbackCacheNotInitialized
            else -> UserFeedbackQagCacheResult.CachedUserFeedback(userFeedbackQagIds)
        }
    }

    override fun initUserFeedbackQagIds(userId: String, qagIds: List<String>) {
        getCache()?.put(userId, qagIds)
    }

    override fun addUserFeedbackQagId(userId: String, qagId: String) {
        getCacheContent(userId = userId)?.let { userFeedbackQagIds ->
            initUserFeedbackQagIds(userId, userFeedbackQagIds + qagId)
        }
    }

    private fun getCache() = cacheManager.getCache(USER_FEEDBACK_QAG_CACHE)

    @Suppress("UNCHECKED_CAST")
    private fun getCacheContent(userId: String) = try {
        getCache()?.get(userId, List::class.java) as? List<String>
    } catch (e: IllegalStateException) {
        null
    }

}