package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagStatus
import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import fr.social.gouv.agora.infrastructure.feedbackQag.repository.FeedbackQagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetFeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val feedbackQagCacheRepository: FeedbackQagCacheRepository,
) : GetFeedbackQagRepository {

    override fun getFeedbackQagStatus(qagId: String, userId: String): FeedbackQagStatus? {
        return try {
            val qagUUID = UUID.fromString(qagId)

            val feedbackQagDto = when (val cacheResult = feedbackQagCacheRepository.getFeedbackQag(qagUUID, userId)) {
                CacheResult.CacheNotInitialized -> getFeedbackQagFromDatabase(qagUUID, userId)
                CacheResult.CachedFeedbackQagNotFound -> null
                is CacheResult.CachedFeedbackQag -> cacheResult.feedbackQagDTO
            }
            FeedbackQagStatus(isExist = (feedbackQagDto != null))
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getFeedbackQagFromDatabase(qagId: UUID, userId: String): FeedbackQagDTO? {
        val feedbackQagDTO = databaseRepository.getFeedbackQag(qagId, userId)
        feedbackQagCacheRepository.insertFeedbackQag(qagId, userId, feedbackQagDTO)
        return feedbackQagDTO
    }
}
