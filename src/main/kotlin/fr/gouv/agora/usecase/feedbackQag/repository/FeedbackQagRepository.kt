package fr.gouv.agora.usecase.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackQagInserting
import java.util.UUID

interface FeedbackQagRepository {
    fun deleteUsersFeedbackQag(userIDs: List<String>)
    fun getFeedbackForQagAndUser(qagId: UUID, userId: UUID): FeedbackQag?
    fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult
    fun updateFeedbackQag(qagId: UUID, userId: UUID, isHelpful: Boolean): FeedbackQagResult
}

enum class FeedbackQagResult {
    SUCCESS, FAILURE
}