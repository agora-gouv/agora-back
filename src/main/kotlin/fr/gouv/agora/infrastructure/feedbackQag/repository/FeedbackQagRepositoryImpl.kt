package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Suppress("unused")
class FeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val mapper: FeedbackQagMapper,
) : FeedbackQagRepository {

    override fun deleteUsersFeedbackQag(userIDs: List<String>) {
        databaseRepository.deleteUsersFeedbackQag(userIDs.mapNotNull { it.toUuidOrNull() })
    }

    override fun getFeedbackResponseForUser(qagId: String, userId: String): Boolean? {
        val qagUuid = qagId.toUuidOrNull() ?: return null
        val userUuid = userId.toUuidOrNull() ?: return null

        return databaseRepository.getFeedbackForQagAndUser(qagId = qagUuid, userId = userUuid)
            .firstOrNull()?.let(mapper::toDomain)?.isHelpful
    }

    override fun getFeedbackQagList(qagId: String): List<FeedbackQag> {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getFeedbackQagList(qagUUID).map(mapper::toDomain)
        } ?: emptyList()
    }

    override fun insertFeedbackQag(feedbackQagInserting: FeedbackQagInserting): FeedbackQagResult {
        return mapper.toDto(feedbackQagInserting)?.let { feedbackQagDTO ->
            databaseRepository.save(feedbackQagDTO)
            FeedbackQagResult.SUCCESS
        } ?: FeedbackQagResult.FAILURE
    }

    override fun updateFeedbackQag(qagId: String, userId: String, isHelpful: Boolean): FeedbackQagResult {
        val qagUuid = qagId.toUuidOrNull() ?: return FeedbackQagResult.FAILURE
        val userUuid = userId.toUuidOrNull() ?: return FeedbackQagResult.FAILURE

        return databaseRepository.getFeedbackForQagAndUser(qagUuid, userUuid).firstOrNull()?.let { dto ->
            databaseRepository.save(mapper.modifyIsHelpful(dto, isHelpful))
            FeedbackQagResult.SUCCESS
        } ?: FeedbackQagResult.FAILURE
    }
}
