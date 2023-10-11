package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagCacheRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import org.springframework.stereotype.Service

@Service
class FeedbackQagUseCase(
    private val feedbackQagRepository: GetFeedbackQagRepository,
    private val cacheRepository: FeedbackQagCacheRepository,
) {

    fun getUserFeedbackQagIds(userId: String): List<String> {
        return cacheRepository.getUserFeedbackQagIds(userId = userId)
            ?: feedbackQagRepository.getUserFeedbackQagIds(userId = userId).also { userFeedbackQagIds ->
                cacheRepository.initUserFeedbackQagIds(userId = userId, qagIds = userFeedbackQagIds)
            }
    }

}