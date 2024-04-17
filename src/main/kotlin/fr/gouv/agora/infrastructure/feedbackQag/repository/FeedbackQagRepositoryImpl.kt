package fr.gouv.agora.infrastructure.feedbackQag.repository

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

    override fun insertOrUpdateFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult {
        val qagId = UUID.fromString(feedbackQagInserting.qagId) ?: return FeedbackQagResult.FAILURE
        val userId = UUID.fromString(feedbackQagInserting.userId) ?: return FeedbackQagResult.FAILURE

        val feedbackId = databaseRepository.getFeedbackForQagAndUser(qagId, userId).firstOrNull()?.id ?: UUID.randomUUID()

        val dto = mapper.toDto(feedbackQagInserting, feedbackId) ?: return FeedbackQagResult.FAILURE

        databaseRepository.save(dto)

        return FeedbackQagResult.SUCCESS
    }

    override fun deleteUsersFeedbackQag(userIDs: List<String>) {
        databaseRepository.deleteUsersFeedbackQag(userIDs.mapNotNull { it.toUuidOrNull() })
    }
}
