package fr.gouv.agora.usecase.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag

interface GetFeedbackQagRepository {
    fun getFeedbackQagList(qagId: String): List<FeedbackQag>
    fun getFeedbackForQagAndUser(qagId: String, userId: String): Boolean?
}