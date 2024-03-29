package fr.gouv.agora.usecase.feedbackQag

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.domain.FeedbackResults
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import org.springframework.stereotype.Service

@Service
class InsertFeedbackQagUseCase(
    private val repository: FeedbackQagRepository,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
    private val userFeedbackCacheRepository: UserFeedbackQagCacheRepository,
    private val feedbackResultsCacheRepository: FeedbackResultsCacheRepository,
    private val feedbackQagUseCase: FeedbackQagUseCase,
) {

    fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagListResult {
        return if (hasAlreadyGivenFeedback(userId = feedbackQagInserting.userId, qagId = feedbackQagInserting.qagId)) {
            FeedbackQagListResult.Failure
        } else {
            when (repository.insertFeedbackQag(feedbackQagInserting)) {
                FeedbackQagResult.SUCCESS -> {
                    userFeedbackCacheRepository.addUserFeedbackQagId(
                        userId = feedbackQagInserting.userId,
                        qagId = feedbackQagInserting.qagId,
                    )
                    if (featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)) {
                        feedbackResultsCacheRepository.evictFeedbackResults(qagId = feedbackQagInserting.qagId)
                        feedbackQagUseCase.getFeedbackResults(qagId = feedbackQagInserting.qagId)
                            ?.let { feedbackResults ->
                                FeedbackQagListResult.Success(feedbackResults)
                            } ?: FeedbackQagListResult.SuccessFeedbackDisabled
                    } else FeedbackQagListResult.SuccessFeedbackDisabled
                }
                else -> FeedbackQagListResult.Failure
            }
        }
    }

    private fun hasAlreadyGivenFeedback(userId: String, qagId: String): Boolean {
        return feedbackQagUseCase.getUserFeedbackQagIds(userId = userId)
            .any { userFeedbackQag -> userFeedbackQag == qagId }
    }
}

sealed class FeedbackQagListResult {
    data class Success(val feedbackResults: FeedbackResults) : FeedbackQagListResult()
    object Failure : FeedbackQagListResult()
    object SuccessFeedbackDisabled : FeedbackQagListResult()
}