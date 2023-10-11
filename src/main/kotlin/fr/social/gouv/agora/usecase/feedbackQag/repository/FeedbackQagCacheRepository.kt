package fr.social.gouv.agora.usecase.feedbackQag.repository

interface FeedbackQagCacheRepository {
    fun getUserFeedbackQagIds(userId: String): List<String>?
    fun initUserFeedbackQagIds(userId: String, qagIds: List<String>): List<String>
}