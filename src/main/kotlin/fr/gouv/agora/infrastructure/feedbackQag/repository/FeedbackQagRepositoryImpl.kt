package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val mapper: FeedbackQagMapper,
) : FeedbackQagRepository {

    override fun deleteUsersFeedbackQag(userIDs: List<String>) {
        databaseRepository.deleteUsersFeedbackQag(userIDs.mapNotNull { it.toUuidOrNull() })
    }

    override fun getFeedbackForQagAndUser(qagId: UUID, userId: UUID): FeedbackQag? {
        return databaseRepository.getFeedbackForQagAndUser(qagId, userId).firstOrNull()?.let { f -> mapper.toDomain(f) }
    }

    override fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult {
        return mapper.toDto(feedbackQagInserting)?.let { feedbackQagDTO ->
            databaseRepository.save(feedbackQagDTO)
            FeedbackQagResult.SUCCESS
        } ?: FeedbackQagResult.FAILURE
    }

    override fun updateFeedbackQag(qagId: UUID, userId: UUID, isHelpful: Boolean): FeedbackQagResult {
        var dto = databaseRepository.getFeedbackForQagAndUser(qagId, userId).firstOrNull() ?: return FeedbackQagResult.FAILURE

        dto = mapper.modifyIsHelpful(dto, isHelpful)

        databaseRepository.save(dto)

        return FeedbackQagResult.SUCCESS
    }
}
