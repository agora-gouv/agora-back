package fr.gouv.agora.usecase.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting

interface FeedbackConsultationUpdateRepository {
    fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting): Boolean
    fun getUserFeedback(userId: String, consultationUpdateId: String): Boolean?
}
