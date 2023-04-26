package fr.social.gouv.agora.usecase.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagInserting

interface FeedbackQagRepository {
    fun insertFeedbackQag(feedbackQag: FeedbackQagInserting): FeedbackQagResult
}
enum class FeedbackQagResult {
    SUCCESS, FAILURE
}