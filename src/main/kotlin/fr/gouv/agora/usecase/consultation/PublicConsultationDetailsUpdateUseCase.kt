package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import org.springframework.stereotype.Component

@Component
class PublicConsultationDetailsUpdateUseCase(
    private val infoRepository: ConsultationInfoRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val historyRepository: ConsultationUpdateHistoryRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {
    fun getConsultationUpdate(
        slugOrIdConsultation: String,
        slugOrIdUpdate: String,
    ): ConsultationDetailsV2WithInfo? {
        val cacheResult = cacheRepository.getConsultationDetails(
            consultationId = slugOrIdConsultation,
            consultationUpdateId = slugOrIdUpdate,
        )

        return when (cacheResult) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            ConsultationUpdateCacheResult.ConsultationUpdateNotFound -> null
            ConsultationUpdateCacheResult.CacheNotInitialized -> buildConsultationDetails(
                consultationIdOrSlug = slugOrIdConsultation,
                consultationUpdateIdOrSlug = slugOrIdUpdate
            ).also { details ->
                cacheRepository.initConsultationDetails(
                    consultationId = slugOrIdConsultation,
                    consultationUpdateId = slugOrIdUpdate,
                    details = details,
                )
            }
        }?.let { details ->
            ConsultationDetailsV2WithInfo(
                consultation = details.consultation,
                update = details.update,
                feedbackStats = details.feedbackStats,
                history = details.history,
                participantCount = if (details.update.hasParticipationInfo || details.update.hasQuestionsInfo) {
                    getParticipantCount(details.consultation.id)
                } else 0,
                isUserFeedbackPositive = null,
            )
        }
    }

    private fun buildConsultationDetails(consultationIdOrSlug: String, consultationUpdateIdOrSlug: String): ConsultationDetailsV2? {
        return infoRepository.getConsultationByIdOrSlug(consultationIdOrSlug)?.let { consultationInfo ->
            updateRepository.getConsultationUpdateBySlugOrId(
                consultationInfo.id,
                consultationUpdateIdOrSlug,
            )?.let { update ->
                ConsultationDetailsV2(
                    consultation = consultationInfo,
                    update = update,
                    feedbackStats = null,
                    history = historyRepository.getConsultationUpdateHistory(consultationInfo.id),
                )
            }
        }
    }

    private fun getParticipantCount(consultationId: String): Int {
        return cacheRepository.getParticipantCount(consultationId) ?: userAnsweredRepository.getParticipantCount(
            consultationId
        ).also { participantCount ->
            cacheRepository.initParticipantCount(consultationId, participantCount)
        }
    }

}
