package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateDeleting
import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto.FeedbackConsultationUpdateDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeedbackConsultationUpdateRepositoryImpl(
    private val databaseRepository: FeedbackConsultationUpdateDatabaseRepository,
    private val mapper: FeedbackConsultationUpdateMapper,
) : FeedbackConsultationUpdateRepository {

    companion object {
        private const val IS_POSITIVE_TRUE_VALUE = 1
    }

    override fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting) {
        mapper.toDto(feedbackInserting)?.let { dto ->
            databaseRepository.save(dto)
        }
    }

    override fun updateFeedback(consultationUpdateId: String, userId: String, isPositive: Boolean): Boolean {
        return getUserFeedbackDto(consultationUpdateId = consultationUpdateId, userId = userId)?.let { dto ->
            databaseRepository.save(mapper.updateFeedback(dto, isPositive))
            true
        } ?: false
    }

    override fun getUserFeedback(consultationUpdateId: String, userId: String): Boolean? {
        return getUserFeedbackDto(
            consultationUpdateId = consultationUpdateId,
            userId = userId,
        )?.isPositive == IS_POSITIVE_TRUE_VALUE
    }

    override fun getFeedbackStats(consultationUpdateId: String): FeedbackConsultationUpdateStats? {
        return consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
            mapper.toStats(databaseRepository.getConsultationUpdateFeedbackStats(consultationUpdateUUID))
        }
    }

    override fun deleteFeedback(feedbackDeleting: FeedbackConsultationUpdateDeleting) {
        feedbackDeleting.consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
            feedbackDeleting.userId.toUuidOrNull()?.let { userUUID ->
                databaseRepository.deleteFeedback(consultationUpdateUUID, userUUID)
            }
        }
    }

    private fun getUserFeedbackDto(consultationUpdateId: String, userId: String): FeedbackConsultationUpdateDTO? {
        return consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
            userId.toUuidOrNull()?.let { userUUID ->
                databaseRepository.getUserConsultationUpdateFeedback(
                    consultationUpdateId = consultationUpdateUUID,
                    userId = userUUID,
                )
            }
        }
    }

}