package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.usecase.consultation.exception.ConsultationUpdateNotFoundException
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateUserFeedbackCacheResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsUpdateV2UseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val infoRepository: ConsultationInfoRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val feedbackRepository: FeedbackConsultationUpdateRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
    private val authentificationHelper: AuthentificationHelper,
    private val historyRepository: ConsultationUpdateHistoryRepository,
) {
    fun getConsultationUnpublishedDetailsUpdate(
        consultationIdOrSlug: String,
        consultationUpdateIdOrSlug: String,
    ): ConsultationDetailsV2WithInfo {
        val consultationInfo = infoRepository.getConsultationByIdOrSlugWithUnpublished(consultationIdOrSlug)
            ?: throw ConsultationUpdateNotFoundException(consultationIdOrSlug, consultationUpdateIdOrSlug)

        val update = updateRepository.getConsultationUpdateBySlugOrIdWithUnpublished(
            consultationInfo.id,
            consultationUpdateIdOrSlug,
        ) ?: throw ConsultationUpdateNotFoundException(consultationIdOrSlug, consultationUpdateIdOrSlug)

        val history = historyRepository.getConsultationUpdateHistory(consultationInfo.id)

        return ConsultationDetailsV2WithInfo(
            consultation = consultationInfo,
            update = update,
            feedbackStats = getFeedbackStats(update),
            history = history,
            participantCount = getParticipantCount(consultationInfo.id),
            isUserFeedbackPositive = getUserFeedback(update),
        )
    }

    private fun getFeedbackStats(consultationUpdate: ConsultationUpdateInfoV2): FeedbackConsultationUpdateStats? {
        if (consultationUpdate.feedbackQuestion == null
            || !featureFlagsRepository.isFeatureEnabled(AgoraFeature.FeedbackConsultationUpdate)
        ) return null
        return feedbackRepository.getFeedbackStats(consultationUpdate.id)
    }

    private fun getParticipantCount(consultationId: String): Int {
        return cacheRepository.getParticipantCount(consultationId) ?: userAnsweredRepository.getParticipantCount(
            consultationId
        ).also { participantCount ->
            cacheRepository.initParticipantCount(consultationId, participantCount)
        }
    }

    private fun getUserFeedback(consultationUpdate: ConsultationUpdateInfoV2): Boolean? {
        val userId = authentificationHelper.getUserId()
        if (userId == null || consultationUpdate.feedbackQuestion == null) return null

        val cacheResult = cacheRepository.getUserFeedback(consultationUpdateId = consultationUpdate.id, userId = userId)
        return when (cacheResult) {
            is ConsultationUpdateUserFeedbackCacheResult.CachedConsultationUpdateUserFeedback -> cacheResult.isUserFeedbackPositive
            ConsultationUpdateUserFeedbackCacheResult.CacheNotInitialized -> feedbackRepository.getUserFeedback(
                consultationUpdateId = consultationUpdate.id, userId = userId
            ).also { userResponse ->
                cacheRepository.initUserFeedback(
                    consultationUpdateId = consultationUpdate.id,
                    userId = userId,
                    isUserFeedbackPositive = userResponse,
                )
            }
        }
    }
}
