package fr.gouv.agora.infrastructure.feedbackQag

import fr.gouv.agora.domain.FeedbackResults
import fr.gouv.agora.infrastructure.qag.FeedbackResultsJson
import org.springframework.stereotype.Component

@Component
class FeedbackJsonMapper {

    fun toJson(feedbackResults: FeedbackResults): FeedbackResultsJson {
        return FeedbackResultsJson(
            positiveRatio = feedbackResults.positiveRatio,
            negativeRatio = feedbackResults.negativeRatio,
            count = feedbackResults.count,
        )
    }

}

