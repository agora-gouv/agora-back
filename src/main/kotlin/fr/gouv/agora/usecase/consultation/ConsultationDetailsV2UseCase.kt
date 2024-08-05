package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateUserFeedbackCacheResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationDetailsV2UseCase(
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val infoRepository: ConsultationInfoRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val feedbackRepository: FeedbackConsultationUpdateRepository,
    private val historyRepository: ConsultationUpdateHistoryRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {
    fun getConsultation(consultationId: String, userId: String): ConsultationDetailsV2WithInfo? {
        val consultationInfo = infoRepository.getConsultation(consultationId)

        val consultationWithInfo = if (consultationInfo != null) {
            val isConsultationOngoing = LocalDateTime.now(clock).isBefore(consultationInfo.endDate)
            val userHasNotAnsweredConsultation =
                !userAnsweredRepository.hasAnsweredConsultation(consultationInfo.id, userId)

            if (isConsultationOngoing && userHasNotAnsweredConsultation) {
                getUnansweredUsersConsultationDetails(consultationInfo = consultationInfo)
            } else {
                getLastConsultationDetails(consultationInfo = consultationInfo)
            }
        } else {
            cacheRepository.initUnansweredUsersConsultationDetails(consultationId, null)
            cacheRepository.initLastConsultationDetails(consultationId, null)
            null
        }

        return consultationWithInfo?.let { details ->
            ConsultationDetailsV2WithInfo(
                consultation = details.consultation,
                update = details.update,
                feedbackStats = details.feedbackStats,
                history = details.history,
                participantCount = if (details.update.hasParticipationInfo || details.update.hasQuestionsInfo) {
                    getParticipantCount(details.consultation.id)
                } else 0,
                isUserFeedbackPositive = getUserFeedback(consultationUpdate = details.update, userId = userId),
            )
        }
    }

    private fun getUnansweredUsersConsultationDetails(
        consultationInfo: ConsultationInfo,
    ): ConsultationDetailsV2? {
        return when (val cacheResult = cacheRepository.getUnansweredUsersConsultationDetails(consultationInfo.id)) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            ConsultationUpdateCacheResult.ConsultationUpdateNotFound -> null
            ConsultationUpdateCacheResult.CacheNotInitialized -> updateRepository.getUnansweredUsersConsultationUpdate(
                consultationId = consultationInfo.id,
            )?.let { update ->
                ConsultationDetailsV2(
                    consultation = consultationInfo,
                    update = update,
                    feedbackStats = getFeedbackStats(update),
                    history = null,
                )
            }
        }.also { details ->
            cacheRepository.initUnansweredUsersConsultationDetails(consultationInfo.id, details)
        }
    }

    private fun getLastConsultationDetails(
        consultationInfo: ConsultationInfo,
    ): ConsultationDetailsV2? {
        return when (val cacheResult = cacheRepository.getLastConsultationDetails(consultationInfo.id)) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            ConsultationUpdateCacheResult.ConsultationUpdateNotFound -> null
            ConsultationUpdateCacheResult.CacheNotInitialized -> updateRepository.getLatestConsultationUpdate(
                consultationId = consultationInfo.id,
            )?.let { update ->
                ConsultationDetailsV2(
                    consultation = consultationInfo,
                    update = update,
                    feedbackStats = getFeedbackStats(update),
                    history = historyRepository.getConsultationUpdateHistory(consultationInfo.id),
                )
            }
        }.also { details ->
            cacheRepository.initLastConsultationDetails(consultationInfo.id, details)
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
