package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.domain.FeedbackQagInserting
import org.springframework.stereotype.Component

@Component
class FeedbackQagResultMapper {

    fun toFeedbackQag(
        feedbackQagInserting: FeedbackQagInserting,
    ): FeedbackQag {
        return FeedbackQag(
            qagId = feedbackQagInserting.qagId,
            userId = feedbackQagInserting.userId,
            isHelpful = feedbackQagInserting.isHelpful,
        )
    }
}