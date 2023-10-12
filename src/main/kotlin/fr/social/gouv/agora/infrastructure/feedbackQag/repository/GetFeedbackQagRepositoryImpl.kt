package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetFeedbackQagRepositoryImpl(
    private val databaseRepository: FeedbackQagDatabaseRepository,
    private val mapper: FeedbackQagMapper,
) : GetFeedbackQagRepository {

    override fun getFeedbackQagList(qagId: String): List<FeedbackQag> {
        return try {
            val qagUUID = UUID.fromString(qagId)
            databaseRepository.getFeedbackQagList(qagUUID).map(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }
}
