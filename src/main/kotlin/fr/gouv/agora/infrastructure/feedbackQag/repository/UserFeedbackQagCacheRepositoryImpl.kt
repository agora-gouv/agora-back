package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheResult
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class UserFeedbackQagCacheRepositoryImpl(
    private val cacheManager: CacheManager,
) : UserFeedbackQagCacheRepository {

    companion object {
        private const val USER_FEEDBACK_QAG_CACHE = "userFeedbackQags"
        private const val USER_FEEDBACK_ANSWERED_YES = "true"
        private const val USER_FEEDBACK_ANSWERED_NO = "false"
        private const val USER_FEEDBACK_NOT_ANSWERED = ""
    }

    override fun getUserFeedbackResponse(userId: String, qagId: String): UserFeedbackQagCacheResult {
        return try {
            when(getCache()?.get("$userId/$qagId", String::class.java)) {
                USER_FEEDBACK_ANSWERED_YES -> UserFeedbackQagCacheResult.CachedUserFeedbackQag(userFeedbackResponse = true)
                USER_FEEDBACK_ANSWERED_NO -> UserFeedbackQagCacheResult.CachedUserFeedbackQag(userFeedbackResponse = false)
                USER_FEEDBACK_NOT_ANSWERED -> UserFeedbackQagCacheResult.CachedUserFeedbackQagNotAnswered
                else -> UserFeedbackQagCacheResult.CacheNotInitialized
            }
        } catch (e: Exception) {
            UserFeedbackQagCacheResult.CacheNotInitialized
        }
    }

    override fun initUserFeedbackResponse(userId: String, qagId: String, userFeedbackResponse: Boolean?) {
        getCache()?.put("$userId/$qagId", userFeedbackResponse?.toString() ?: USER_FEEDBACK_NOT_ANSWERED)
    }

    private fun getCache() = cacheManager.getCache(USER_FEEDBACK_QAG_CACHE)

}