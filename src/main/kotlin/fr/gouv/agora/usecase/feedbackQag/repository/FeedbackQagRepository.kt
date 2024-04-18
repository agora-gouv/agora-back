package fr.gouv.agora.usecase.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackQagInserting

interface FeedbackQagRepository {
    fun deleteUsersFeedbackQag(userIDs: List<String>)
    fun getFeedbackResponseForUser(qagId: String, userId: String): Boolean?
    fun getFeedbackQagList(qagId: String): List<FeedbackQag>
    fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult
    fun updateFeedbackQag(qagId: String, userId: String, isHelpful: Boolean): FeedbackQagResult
}

enum class FeedbackQagResult {
    SUCCESS, FAILURE
}