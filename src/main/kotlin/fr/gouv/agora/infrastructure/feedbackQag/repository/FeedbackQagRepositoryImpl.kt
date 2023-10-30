package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val mapper: FeedbackQagMapper,
) : FeedbackQagRepository {

    override fun insertFeedbackQag(feedbackQag: FeedbackQagInserting): FeedbackQagResult {
        return mapper.toDto(feedbackQag)?.let { feedbackQagDTO ->
            databaseRepository.save(feedbackQagDTO)
            FeedbackQagResult.SUCCESS
        } ?: FeedbackQagResult.FAILURE
    }
}
