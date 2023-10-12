package fr.social.gouv.agora.usecase.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.domain.FeedbackQagStatus

interface GetFeedbackQagRepository {
    fun getFeedbackQagStatus(qagId: String, userId: String): FeedbackQagStatus?
    fun getFeedbackQagList(qagId: String): List<FeedbackQagInserting>
}