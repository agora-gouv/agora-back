package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.stereotype.Component

@Component
class FeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val mapper: FeedbackQagMapper,
    private val feedbackQagCacheRepository: FeedbackQagCacheRepository,
) : FeedbackQagRepository {

    companion object {
        private const val MAX_INSERT_RETRY_COUNT = 3
    }

    override fun insertFeedbackQag(feedbackQag: FeedbackQagInserting): FeedbackQagResult {
        repeat(MAX_INSERT_RETRY_COUNT) {
            mapper.toDto(feedbackQag)?.let { feedbackQagDTO ->
                if (!databaseRepository.existsById(feedbackQagDTO.id)) {
                    databaseRepository.save(feedbackQagDTO)
                    feedbackQagCacheRepository.insertFeedbackQag(
                        feedbackQagDTO.qagId,
                        feedbackQagDTO.userId,
                        feedbackQagDTO
                    )
                    return FeedbackQagResult.SUCCESS
                }
            }
        }
        return FeedbackQagResult.FAILURE
    }
}
