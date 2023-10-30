package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.FeedbackResults
import fr.social.gouv.agora.usecase.feedbackQag.repository.*
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class FeedbackQagUseCase(
    private val feedbackQagRepository: GetFeedbackQagRepository,
    private val resultsCacheRepository: FeedbackResultsCacheRepository,
    private val userFeedbackCacheRepository: UserFeedbackQagCacheRepository,
) {

    fun getFeedbackResults(qagId: String): FeedbackResults {
        return when (val cacheResult = resultsCacheRepository.getFeedbackResults(qagId = qagId)) {
            is FeedbackResultsCacheResult.CachedFeedbackResults -> cacheResult.feedbackResults
            FeedbackResultsCacheResult.FeedbackResultsCacheNotInitialized -> buildResults(qagId = qagId).also {
                resultsCacheRepository.initFeedbackResults(qagId = qagId, results = it)
            }
        }
    }

    fun getUserFeedbackQagIds(userId: String): List<String> {
        return when (val cacheResult = userFeedbackCacheRepository.getUserFeedbackQagIds(userId = userId)) {
            is UserFeedbackQagCacheResult.CachedUserFeedback -> cacheResult.userFeedbackQagIds
            UserFeedbackQagCacheResult.UserFeedbackCacheNotInitialized -> feedbackQagRepository
                .getUserFeedbackQagIds(userId = userId).also {
                    userFeedbackCacheRepository.initUserFeedbackQagIds(userId = userId, qagIds = it)
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