package fr.gouv.agora.infrastructure.feedbackConsultationUpdate

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateResults
import org.springframework.stereotype.Component

@Component
class FeedbackConsultationUpdateJsonMapper {

    fun toInserting(
        json: InsertFeedbackConsultationUpdateJson,
        userId: String,
        consultationUpdateId: String,
    ): FeedbackConsultationUpdateInserting {
        return FeedbackConsultationUpdateInserting(
            userId = userId,
            consultationUpdateId = consultationUpdateId,
            isPositive = json.isPositive,
        )
    }

    fun toJson(results: FeedbackConsultationUpdateResults): FeedbackConsultationUpdateResultsJson {
        return FeedbackConsultationUpdateResultsJson(
            userResponse = results.userResponse,
            stats = results.stats?.let { stats ->
                FeedbackConsultationUpdateStatsJson(
                    positiveRatio = stats.positiveRatio,
                    negativeRatio = stats.negativeRatio,
                    responseCount = stats.responseCount,
                )
            },
        )
    }

}