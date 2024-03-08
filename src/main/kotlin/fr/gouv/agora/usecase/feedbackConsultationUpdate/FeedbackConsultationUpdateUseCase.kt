package fr.gouv.agora.usecase.feedbackConsultationUpdate

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateResults
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.springframework.stereotype.Component

@Component
class FeedbackConsultationUpdateUseCase(
    private val feedbackConsultationUpdateRepository: FeedbackConsultationUpdateRepository,
) {

    fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting): InsertFeedbackConsultationUpdateResults {
        // TODO: verify if update has feedback
        return if (feedbackConsultationUpdateRepository.insertFeedback(feedbackInserting)) {
            InsertFeedbackConsultationUpdateResults.Success(
                FeedbackConsultationUpdateResults(
                    userResponse = feedbackInserting.isPositive,
                    stats = buildFeedbackStats(consultationUpdateId = feedbackInserting.consultationUpdateId),
                )
            )
        } else {
            InsertFeedbackConsultationUpdateResults.Failure
        }
    }

    private fun buildFeedbackStats(consultationUpdateId: String): FeedbackConsultationUpdateStats? {
        // TODO
        return null
    }

}

sealed class InsertFeedbackConsultationUpdateResults {
    data class Success(val feedbackResults: FeedbackConsultationUpdateResults) :
        InsertFeedbackConsultationUpdateResults()

    object Failure : InsertFeedbackConsultationUpdateResults()
}