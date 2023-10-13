package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import org.springframework.stereotype.Service

@Service
class InsertFeedbackQagUseCase(
    private val repository: FeedbackQagRepository,
    private val getFeedbackQagRepository: GetFeedbackQagRepository,
) {
    fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult {
        return if (getFeedbackQagRepository.getFeedbackQagList(qagId = feedbackQagInserting.qagId)
                .any { feedbackQag -> feedbackQag.userId == feedbackQagInserting.userId }
        )
            FeedbackQagResult.FAILURE
        else
            repository.insertFeedbackQag(feedbackQagInserting)
    }
}