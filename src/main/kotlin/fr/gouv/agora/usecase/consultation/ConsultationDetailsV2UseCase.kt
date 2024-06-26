package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.*
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationDetailsV2UseCase(
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val infoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val feedbackRepository: FeedbackConsultationUpdateRepository,
    private val historyRepository: ConsultationUpdateHistoryRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {

    fun getConsultation(consultationId: String, userId: String): ConsultationDetailsV2WithInfo? {
        return getConsultationDetails(consultationId, userId)?.let { details ->
            ConsultationDetailsV2WithInfo(
                consultation = details.consultation,
                thematique = details.thematique,
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

    private fun getConsultationDetails(consultationId: String, userId: String): ConsultationDetailsV2? {
        return infoRepository.getConsultation(consultationId)?.let { consultationInfo ->
            thematiqueRepository.getThematique(consultationInfo.thematiqueId)?.let { thematique ->
                if (shouldUseUnansweredUsersUpdate(consultationInfo = consultationInfo, userId = userId)) {
                    getUnansweredUsersConsultationDetails(
                        consultationInfo = consultationInfo,
                        thematique = thematique,
                    )
                } else {
                    getLastConsultationDetails(
                        consultationInfo = consultationInfo,
                        thematique = thematique,
                    )
                }
            }
        } ?: run {
            cacheRepository.initUnansweredUsersConsultationDetails(consultationId, null)
            cacheRepository.initLastConsultationDetails(consultationId, null)
            null
        }
    }

    private fun shouldUseUnansweredUsersUpdate(consultationInfo: ConsultationInfo, userId: String): Boolean {
        val isConsultationOngoing = LocalDateTime.now(clock).isBefore(consultationInfo.endDate)
        return isConsultationOngoing && !hasUserAnsweredConsultation(
            consultationId = consultationInfo.id,
            userId = userId,
        )
    }

    private fun hasUserAnsweredConsultation(consultationId: String, userId: String): Boolean {
        return cacheRepository.hasAnsweredConsultation(consultationId = consultationId, userId = userId)
            ?: userAnsweredRepository.hasAnsweredConsultation(
                consultationId = consultationId,
                userId = userId,
            ).also { hasAnswered ->
                cacheRepository.initHasAnsweredConsultation(
                    consultationId = consultationId,
                    userId = userId,
                    hasAnswered = hasAnswered,
                )
            }
    }

    private fun getUnansweredUsersConsultationDetails(
        consultationInfo: ConsultationInfo,
        thematique: Thematique,
    ): ConsultationDetailsV2? {
        return when (val cacheResult = cacheRepository.getUnansweredUsersConsultationDetails(consultationInfo.id)) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            ConsultationUpdateCacheResult.ConsultationUpdateNotFound -> null
            ConsultationUpdateCacheResult.CacheNotInitialized -> updateRepository.getUnansweredUsersConsultationUpdate(
                consultationId = consultationInfo.id,
            )?.let { update ->
                ConsultationDetailsV2(
                    consultation = consultationInfo,
                    thematique = thematique,
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
        thematique: Thematique,
    ): ConsultationDetailsV2? {
        return when (val cacheResult = cacheRepository.getLastConsultationDetails(consultationInfo.id)) {
            is ConsultationUpdateCacheResult.CachedConsultationsDetails -> cacheResult.details
            ConsultationUpdateCacheResult.ConsultationUpdateNotFound -> null
            ConsultationUpdateCacheResult.CacheNotInitialized -> updateRepository.getLatestConsultationUpdate(
                consultationId = consultationInfo.id,
            )?.let { update ->
                ConsultationDetailsV2(
                    consultation = consultationInfo,
                    thematique = thematique,
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
