package fr.social.gouv.agora.usecase.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag

interface FeedbackQagRepository {
    fun insertFeedbackQag(feedbackQag: FeedbackQag): FeedbackQagResult
}
enum class FeedbackQagResult {
    SUCCESS, FAILURE
}