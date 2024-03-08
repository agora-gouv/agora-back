package fr.gouv.agora.usecase.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats

interface FeedbackConsultationUpdateRepository {
    fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting)
    fun getUserFeedback(consultationUpdateId: String, userId: String): Boolean?
    fun getFeedbackStats(consultationUpdateId: String): FeedbackConsultationUpdateStats?
}
