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
    private val userRepository: UserAnsweredConsultationRepository,
    private val feedbackRepository: FeedbackConsultationUpdateRepository,
    private val historyRepository: ConsultationUpdateHistoryRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {
    fun getConsultation(consultationId: String, userId: String): ConsultationDetailsV2WithInfo? {
        val consultationInfo = infoRepository.getConsultation(consultationId)

        if (consultationInfo == null) {
            cacheRepository.initUnansweredUsersConsultationDetails(consultationId, null)
            cacheRepository.initLastConsultationDetails(consultationId, null)
            return null
        }

        val isConsultationOngoing = LocalDateTime.now(clock).isBefore(consultationInfo.endDate)
        val userHasNotAnsweredConsultation = !userRepository.hasAnsweredConsultation(consultationInfo.id, userId)

        val consultationWithInfo = if (isConsultationOngoing && userHasNotAnsweredConsultation) {
            getUnansweredUsersConsultationDetails(consultationInfo = consultationInfo)
        } else {
            getLastConsultationDetails(consultationInfo = consultationInfo)
        }

        if (consultationWithInfo == null) {
            return null
        }

        val hasInfo = consultationWithInfo.update.hasParticipationInfo || consultationWithInfo.update.hasQuestionsInfo
        val participantCount = if (hasInfo) getParticipantCount(consultationWithInfo.consultation.id) else 0

        val isUserFeedbackPositive = if (consultationWithInfo.update.feedbackQuestion != null) {
            feedbackRepository.getUserFeedback(consultationWithInfo.update.id, userId)
        } else null

        return ConsultationDetailsV2WithInfo(
            consultation = consultationWithInfo.consultation,
            update = consultationWithInfo.update,
            feedbackStats = consultationWithInfo.feedbackStats,
            history = consultationWithInfo.history,
            participantCount = participantCount,
            isUserFeedbackPositive = isUserFeedbackPositive,
        )
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
                ConsultationDetailsV2(consultationInfo, update, getFeedbackStats(update), null)
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
        return cacheRepository.getParticipantCount(consultationId) ?: userRepository.getParticipantCount(
            consultationId
        ).also { participantCount ->
            cacheRepository.initParticipantCount(consultationId, participantCount)
        }
    }
}
