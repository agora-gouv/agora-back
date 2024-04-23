package fr.gouv.agora.usecase.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateDeleting
import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats

interface FeedbackConsultationUpdateRepository {
    fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting)
    fun updateFeedback(consultationUpdateId: String, userId: String, isPositive: Boolean): Boolean
    fun getUserFeedback(consultationUpdateId: String, userId: String): Boolean?
    fun getFeedbackStats(consultationUpdateId: String): FeedbackConsultationUpdateStats?
    fun deleteFeedback(feedbackDeleting: FeedbackConsultationUpdateDeleting)
}
