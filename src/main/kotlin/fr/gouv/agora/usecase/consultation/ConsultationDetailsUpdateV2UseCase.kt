package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateUserFeedbackCacheResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsUpdateV2UseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val infoRepository: ConsultationInfoRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val feedbackRepository: FeedbackConsultationUpdateRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {

    fun getConsultationDetailsUpdate(
        consultationId: String,
        consultationUpdateId: String,
        userId: String,
    ): ConsultationDetailsV2WithInfo? {
        val cacheResult = cacheRepository.getConsultationDetails(
            consultationId = consultationId,
            consultationUpdateId = consultationUpdateId,
        )

        return when (cacheResult) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            ConsultationUpdateCacheResult.ConsultationUpdateNotFound -> null
            ConsultationUpdateCacheResult.CacheNotInitialized -> buildConsultationDetails(
                consultationId = consultationId,
                consultationUpdateId = consultationUpdateId
            ).also { details ->
                cacheRepository.initConsultationDetails(
                    consultationId = consultationId,
                    consultationUpdateId = consultationUpdateId,
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
                    getParticipantCount(consultationId)
                } else 0,
                isUserFeedbackPositive = getUserFeedback(consultationUpdate = details.update, userId = userId),
            )
        }
    }

    private fun buildConsultationDetails(consultationId: String, consultationUpdateId: String): ConsultationDetailsV2? {
        return infoRepository.getConsultation(consultationId)?.let { consultationInfo ->
                updateRepository.getConsultationUpdate(
                    consultationId = consultationId,
                    consultationUpdateId = consultationUpdateId,
                )?.let { update ->
                    ConsultationDetailsV2(
                        consultation = consultationInfo,
                        update = update,
                        feedbackStats = getFeedbackStats(update),
                        history = null,
                    )
                }
        }
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

    private fun getUserFeedback(consultationUpdate: ConsultationUpdateInfoV2, userId: String): Boolean? {
        if (consultationUpdate.feedbackQuestion == null) return null

        val cacheResult = cacheRepository.getUserFeedback(consultationUpdateId = consultationUpdate.id, userId = userId)
        return when (cacheResult) {
            is ConsultationUpdateUserFeedbackCacheResult.CachedConsultationUpdateUserFeedback -> cacheResult.isUserFeedbackPositive
            ConsultationUpdateUserFeedbackCacheResult.ConsultationUpdateUserFeedbackNotFound -> null
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
