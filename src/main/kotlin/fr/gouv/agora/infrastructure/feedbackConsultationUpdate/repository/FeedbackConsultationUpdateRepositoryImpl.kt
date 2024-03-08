package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class FeedbackConsultationUpdateRepositoryImpl(
    private val databaseRepository: FeedbackConsultationUpdateDatabaseRepository,
    private val mapper: FeedbackConsultationUpdateMapper,
) : FeedbackConsultationUpdateRepository {

    companion object {
        private const val IS_POSITIVE_TRUE_VALUE = 1
    }

    override fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting): Boolean {
        return mapper.toDto(feedbackInserting)?.let { dto ->
            if (getUserFeedback(
                    userId = dto.userId,
                    consultationUpdateId = dto.consultationUpdateId,
                ) != null
            ) return false
            databaseRepository.save(dto)
            true
        } ?: false
    }

    override fun getUserFeedback(userId: String, consultationUpdateId: String): Boolean? {
        return userId.toUuidOrNull()?.let { userUUID ->
            consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
                getUserFeedback(userId = userUUID, consultationUpdateId = consultationUpdateUUID)
            }
        }
    }

    private fun getUserFeedback(userId: UUID, consultationUpdateId: UUID): Boolean? {
        return databaseRepository.getUserConsultationUpdateFeedback(
            userId = userId,
            consultationUpdateId = consultationUpdateId,
        ).firstOrNull()?.let { isPositiveValue -> isPositiveValue == IS_POSITIVE_TRUE_VALUE }
    }

}