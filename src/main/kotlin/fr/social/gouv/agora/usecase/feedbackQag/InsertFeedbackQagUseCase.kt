package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import org.springframework.stereotype.Service

@Service
class InsertFeedbackQagUseCase(
    private val repository: FeedbackQagRepository,
    private val getFeedbackQagRepository: GetFeedbackQagRepository,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {
    fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagListResult {
        return if (getFeedbackQagRepository.getFeedbackQagList(qagId = feedbackQagInserting.qagId)
                .any { feedbackQag -> feedbackQag.userId == feedbackQagInserting.userId }
        )
            FeedbackQagListResult.Failure
        else {
            when (repository.insertFeedbackQag(feedbackQagInserting)) {
                FeedbackQagResult.SUCCESS -> if (featureFlagsUseCase.isFeatureEnabled(AgoraFeature.FeedbackResponseQag))
                    FeedbackQagListResult.Success(getFeedbackQagRepository.getFeedbackQagList(qagId = feedbackQagInserting.qagId))
                else FeedbackQagListResult.Success(null)

                else -> FeedbackQagListResult.Failure
            }
        }
    }
}

sealed class FeedbackQagListResult {
    data class Success(val feedbackQagList: List<FeedbackQag>?) : FeedbackQagListResult()
    object Failure : FeedbackQagListResult()
}