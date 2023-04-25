package fr.social.gouv.agora.usecase.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.domain.FeedbackQagStatus

interface GetFeedbackQagRepository {
    fun getFeedbackQagStatus(feedbackQag: FeedbackQag) : FeedbackQagStatus?
}