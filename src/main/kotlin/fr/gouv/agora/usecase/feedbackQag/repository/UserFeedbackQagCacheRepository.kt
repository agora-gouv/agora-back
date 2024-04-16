package fr.gouv.agora.usecase.feedbackQag.repository

interface UserFeedbackQagCacheRepository {
    fun initUserFeedbackQagIds(userId: String, qagIds: List<String>)
    fun addUserFeedbackQagId(userId: String, qagId: String)
}