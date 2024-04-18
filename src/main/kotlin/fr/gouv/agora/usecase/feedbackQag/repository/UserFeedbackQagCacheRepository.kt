package fr.gouv.agora.usecase.feedbackQag.repository

interface UserFeedbackQagCacheRepository {
    fun getUserFeedbackResponse(userId: String, qagId: String): UserFeedbackQagCacheResult
    fun initUserFeedbackResponse(userId: String, qagId: String, userFeedbackResponse: Boolean?)
}

sealed class UserFeedbackQagCacheResult {
    object CacheNotInitialized : UserFeedbackQagCacheResult()
    data class CachedUserFeedbackQag(val userFeedbackResponse: Boolean) : UserFeedbackQagCacheResult()
    object CachedUserFeedbackQagNotAnswered : UserFeedbackQagCacheResult()
}
