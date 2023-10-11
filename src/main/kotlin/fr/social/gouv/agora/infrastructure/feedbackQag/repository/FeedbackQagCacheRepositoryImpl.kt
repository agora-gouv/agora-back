package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagCacheRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeedbackQagCacheRepositoryImpl : FeedbackQagCacheRepository {

    override fun getUserFeedbackQagIds(userId: String): List<String>? {
        TODO("Not yet implemented")
    }

    override fun initUserFeedbackQagIds(userId: String, qagIds: List<String>): List<String> {
        TODO("Not yet implemented")
    }

}