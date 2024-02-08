package fr.gouv.agora.usecase.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQagInserting

interface FeedbackQagRepository {
    fun insertFeedbackQag(feedbackQag: FeedbackQagInserting): FeedbackQagResult
    fun deleteUsersFeedbackQag(userIDs: List<String>)
}

enum class FeedbackQagResult {
    SUCCESS, FAILURE
}