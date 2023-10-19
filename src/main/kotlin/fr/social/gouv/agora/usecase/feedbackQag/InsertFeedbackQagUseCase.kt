package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.usecase.feedbackQag.repository.*
import org.springframework.stereotype.Service

@Service
class InsertFeedbackQagUseCase(
    private val repository: FeedbackQagRepository,
    private val getFeedbackQagRepository: GetFeedbackQagRepository,
    private val userFeedbackCacheRepository: UserFeedbackQagCacheRepository,
    private val feedbackResultsCacheRepository: FeedbackResultsCacheRepository,
) {
    fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult {
        return if (hasAlreadyGivenFeedback(feedbackQagInserting)) FeedbackQagResult.FAILURE
        else {
            userFeedbackCacheRepository.addUserFeedbackQagId(
                userId = feedbackQagInserting.userId,
                qagId = feedbackQagInserting.qagId,
            )
            feedbackResultsCacheRepository.evictFeedbackResults(qagId = feedbackQagInserting.qagId)
            repository.insertFeedbackQag(feedbackQagInserting)
        }
    }

    private fun hasAlreadyGivenFeedback(feedbackQagInserting: FeedbackQagInserting) =
        getFeedbackQagRepository.getFeedbackQagList(qagId = feedbackQagInserting.qagId)
            .any { feedbackQag -> feedbackQag.userId == feedbackQagInserting.userId }
}