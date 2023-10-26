package fr.gouv.agora.usecase.feedbackQag.repository

interface UserFeedbackQagCacheRepository {
    fun getUserFeedbackQagIds(userId: String): UserFeedbackQagCacheResult
    fun initUserFeedbackQagIds(userId: String, qagIds: List<String>)
    fun addUserFeedbackQagId(userId: String, qagId: String)
}

sealed class UserFeedbackQagCacheResult {
    data class CachedUserFeedback(val userFeedbackQagIds: List<String>) : UserFeedbackQagCacheResult()
    object UserFeedbackCacheNotInitialized : UserFeedbackQagCacheResult()
}