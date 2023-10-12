package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagInserting
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
    private val mapper: FeedbackQagMapper,
) : GetFeedbackQagRepository {

    override fun getFeedbackQagStatus(qagId: String, userId: String): FeedbackQagStatus? {
        return try {
            val qagUUID = UUID.fromString(qagId)
            val userUUID = UUID.fromString(userId)

            val feedbackQagDto = when (val cacheResult = feedbackQagCacheRepository.getFeedbackQag(
                qagId = qagUUID,
                userId = userUUID
            )) {
                CacheResult.CacheNotInitialized -> getFeedbackQagFromDatabase(qagId = qagUUID, userId = userUUID)
                CacheResult.CachedFeedbackQagNotFound -> null
                is CacheResult.CachedFeedbackQag -> cacheResult.feedbackQagDTO
            }
            FeedbackQagStatus(isExist = (feedbackQagDto != null))
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun getFeedbackQagList(qagId: String): List<FeedbackQagInserting> {
        return try {
            val qagUUID = UUID.fromString(qagId)
            databaseRepository.getFeedbackQagList(qagUUID).map(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getFeedbackQagFromDatabase(qagId: UUID, userId: UUID): FeedbackQagDTO? {
        val feedbackQagDTO = databaseRepository.getFeedbackQag(qagId = qagId, userId = userId)
        feedbackQagCacheRepository.insertFeedbackQag(qagId = qagId, userId = userId, feedbackQagDTO = feedbackQagDTO)
        return feedbackQagDTO
    }
}
