package fr.gouv.agora.infrastructure.feedbackQag.repository

import fr.gouv.agora.domain.FeedbackQag
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import org.springframework.stereotype.Component

@Component
class GetFeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val mapper: FeedbackQagMapper,
) : GetFeedbackQagRepository {

    override fun getFeedbackQagList(qagId: String): List<FeedbackQag> {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getFeedbackQagList(qagUUID).map(mapper::toDomain)
        } ?: emptyList()
    }

    override fun getFeedbackForQagAndUser(qagId: String, userId: String): Boolean? {
        val qagUuid = qagId.toUuidOrNull() ?: return null
        val userUuid = userId.toUuidOrNull() ?: return null

        return databaseRepository.getFeedbackForQagAndUser(qagId = qagUuid, userId = userUuid)
            .map(mapper::toDomain).firstOrNull()?.isHelpful
    }
}
