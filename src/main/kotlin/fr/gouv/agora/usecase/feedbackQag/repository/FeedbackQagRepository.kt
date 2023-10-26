package fr.gouv.agora.usecase.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQagInserting

interface FeedbackQagRepository {
    fun insertFeedbackQag(feedbackQag: FeedbackQagInserting): FeedbackQagResult
}
enum class FeedbackQagResult {
    SUCCESS, FAILURE
}