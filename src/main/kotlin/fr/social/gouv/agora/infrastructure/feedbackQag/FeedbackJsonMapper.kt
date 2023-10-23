package fr.social.gouv.agora.infrastructure.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.infrastructure.qag.FeedbackResultsJson
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class FeedbackJsonMapper {

    fun toJson(feedbackList: List<FeedbackQag>): FeedbackResultsJson {
        val (positiveRatio, negativeRatio) = if (feedbackList.isEmpty()) 0 to 0
        else {
            val positiveRatio =
                (feedbackList.count { feedbackQag -> feedbackQag.isHelpful } * 100.0 / feedbackList.size).roundToInt()
            val negativeRatio = 100 - positiveRatio
            positiveRatio to negativeRatio
        }
        return FeedbackResultsJson(
            positiveRatio = positiveRatio,
            negativeRatio = negativeRatio,
            count = feedbackList.size,
        )
    }
}

