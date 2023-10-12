package fr.social.gouv.agora.usecase.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag

interface GetFeedbackQagRepository {
    fun getFeedbackQagList(qagId: String): List<FeedbackQag>
}