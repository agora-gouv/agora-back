package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.AgoraFeature.FeedbackConsultationUpdate
import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.usecase.consultation.exception.ConsultationNotFoundException
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
    private val authentificationHelper: AuthentificationHelper,
) {
    fun getConsultation(consultationIdOrSlug: String): ConsultationDetailsV2WithInfo {
        val consultationInfo = if (authentificationHelper.canViewUnpublishedConsultations()) {
            infoRepository.getConsultationByIdOrSlugWithUnpublished(consultationIdOrSlug)
        } else {
            infoRepository.getConsultationByIdOrSlug(consultationIdOrSlug)
        }
        if (consultationInfo == null) throw ConsultationNotFoundException(consultationIdOrSlug)

        val consultationWithInfo = getConsultationDetails(consultationInfo)

        return ConsultationDetailsV2WithInfo(
            consultation = consultationWithInfo.consultation,
            update = consultationWithInfo.update,
            feedbackStats = consultationWithInfo.feedbackStats,
            history = consultationWithInfo.history,
            participantCount = getParticipantCount(consultationWithInfo),
            isUserFeedbackPositive = getUserFeedback(consultationWithInfo),
        )
    }

    private fun getUserFeedback(consultationWithInfo: ConsultationDetailsV2): Boolean? {
        val userId = authentificationHelper.getUserId()
        if (userId == null || consultationWithInfo.update.feedbackQuestion == null) return null

        return feedbackRepository.getUserFeedback(consultationWithInfo.update.id, userId)
    }

    private fun getConsultationDetails(consultationInfo: ConsultationInfo): ConsultationDetailsV2 {
        val userId = authentificationHelper.getUserId()
        val userHasNotAnsweredConsultation =
            userId == null || !userRepository.hasAnsweredConsultation(consultationInfo.id, userId)
        val now = LocalDateTime.now(clock)

        val consultationWithInfo = if (consultationInfo.isOngoing(now) && userHasNotAnsweredConsultation) {
            getUnansweredUsersConsultationDetails(consultationInfo = consultationInfo)
        } else {
            getLastConsultationDetails(consultationInfo = consultationInfo)
        }
        if (consultationWithInfo == null) throw ConsultationNotFoundException(consultationInfo.id)

        return consultationWithInfo
    }

    private fun getUnansweredUsersConsultationDetails(
        consultationInfo: ConsultationInfo,
    ): ConsultationDetailsV2? {
        val cachedConsultationDetails = cacheRepository.getUnansweredUsersConsultationDetails(consultationInfo.id)
        val consultationDetails = when (cachedConsultationDetails) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cachedConsultationDetails.details
            is ConsultationUpdateCacheResult.CacheNotInitialized -> {
                updateRepository.getUnansweredUsersConsultationUpdateWithUnpublished(consultationInfo.id)
                    ?.let { ConsultationDetailsV2(consultationInfo, it, getFeedbackStats(it), null) }
            }
        }
        cacheRepository.initUnansweredUsersConsultationDetails(consultationInfo.id, consultationDetails)

        return consultationDetails
    }

    private fun getLastConsultationDetails(
        consultationInfo: ConsultationInfo,
    ): ConsultationDetailsV2? {
        val details = when (val cacheResult = cacheRepository.getLastConsultationDetails(consultationInfo.id)) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            is ConsultationUpdateCacheResult.CacheNotInitialized -> {
                updateRepository.getLatestConsultationUpdate(consultationInfo.id)
                    ?.let { update ->
                        val history = historyRepository.getConsultationUpdateHistory(consultationInfo.id)
                        ConsultationDetailsV2(consultationInfo, update, getFeedbackStats(update), history)
                    }
            }
        }
        cacheRepository.initLastConsultationDetails(consultationInfo.id, details)

        return details
    }

    private fun getFeedbackStats(consultationUpdate: ConsultationUpdateInfoV2): FeedbackConsultationUpdateStats? {
        if (!featureFlagsRepository.isFeatureEnabled(FeedbackConsultationUpdate) || consultationUpdate.feedbackQuestion == null)
            return null

        return feedbackRepository.getFeedbackStats(consultationUpdate.id)
    }

    private fun getParticipantCount(consultationWithInfo: ConsultationDetailsV2): Int {
        if (!consultationWithInfo.hasQuestionsOrParticipationInfos()) return 0

        val consultationId = consultationWithInfo.getConsultationId()
        val participantCount = cacheRepository.getParticipantCount(consultationId)
            ?: userRepository.getParticipantCount(consultationId)
        cacheRepository.initParticipantCount(consultationId, participantCount)

        return participantCount
    }
}
