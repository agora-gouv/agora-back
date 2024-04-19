package fr.gouv.agora.usecase.feedbackQag

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.FeedbackResults
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackResultsCacheResult
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheRepository
import fr.gouv.agora.usecase.feedbackQag.repository.UserFeedbackQagCacheResult
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class FeedbackQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val feedbackQagRepository: FeedbackQagRepository,
    private val resultsCacheRepository: FeedbackResultsCacheRepository,
    private val userFeedbackQagCacheRepository: UserFeedbackQagCacheRepository,
) {

    fun getFeedbackResults(qagId: String): FeedbackResults? {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.FeedbackResponseQag)) return null
        return when (val cacheResult = resultsCacheRepository.getFeedbackResults(qagId = qagId)) {
            is FeedbackResultsCacheResult.CachedFeedbackResults -> cacheResult.feedbackResults
            FeedbackResultsCacheResult.FeedbackResultsCacheNotInitialized -> buildResults(qagId = qagId).also {
                resultsCacheRepository.initFeedbackResults(qagId = qagId, results = it)
            }
        }
    }

    fun getFeedbackForQagAndUser(qagId: String, userId: String): Boolean? {
        return when(val cacheResult = userFeedbackQagCacheRepository.getUserFeedbackResponse(qagId = qagId, userId = userId)) {
            is UserFeedbackQagCacheResult.CachedUserFeedbackQag -> cacheResult.userFeedbackResponse
            UserFeedbackQagCacheResult.CachedUserFeedbackQagNotAnswered -> null
            UserFeedbackQagCacheResult.CacheNotInitialized -> feedbackQagRepository.getFeedbackResponseForUser(qagId = qagId, userId = userId).also {
                userFeedbackQagCacheRepository.initUserFeedbackResponse(
                    userId = userId,
                    qagId = qagId,
                    userFeedbackResponse = it,
                )
            }
        }
    }

    private fun buildResults(qagId: String): FeedbackResults {
        val feedbackList = feedbackQagRepository.getFeedbackQagList(qagId = qagId)
        val (positiveRatio, negativeRatio) = if (feedbackList.isEmpty()) 0 to 0
        else {
            val positiveRatio =
                (feedbackList.count { feedbackQag -> feedbackQag.isHelpful } * 100.0 / feedbackList.size).roundToInt()
            val negativeRatio = 100 - positiveRatio
            positiveRatio to negativeRatio
        }

        return FeedbackResults(
            positiveRatio = positiveRatio,
            negativeRatio = negativeRatio,
            count = feedbackList.size,
        )
    }

}