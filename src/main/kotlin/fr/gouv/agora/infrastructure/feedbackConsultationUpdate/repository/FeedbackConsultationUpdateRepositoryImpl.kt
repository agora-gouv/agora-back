package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
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

    override fun getUserFeedback(consultationUpdateId: String, userId: String): Boolean? {
        return consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
            userId.toUuidOrNull()?.let { userUUID ->
                databaseRepository.getUserConsultationUpdateFeedback(
                    consultationUpdateId = consultationUpdateUUID,
                    userId = userUUID,
                ).firstOrNull()?.let { isPositiveValue -> isPositiveValue == IS_POSITIVE_TRUE_VALUE }
            }
        }
    }

    override fun getFeedbackStats(consultationUpdateId: String): FeedbackConsultationUpdateStats? {
        return consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
            mapper.toStats(databaseRepository.getConsultationUpdateFeedbackStats(consultationUpdateUUID))
        }
    }

}