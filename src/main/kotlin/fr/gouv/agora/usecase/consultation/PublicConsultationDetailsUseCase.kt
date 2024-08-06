package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class PublicConsultationDetailsUseCase(
    private val clock: Clock,
    private val infoRepository: ConsultationInfoRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val historyRepository: ConsultationUpdateHistoryRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {
    fun getConsultation(slugOrId: String): ConsultationDetailsV2WithInfo? {
        // Pour le moment on récupère soit par slug, soit par ID, pour ne pas avoir à obliger la mise à jour
        // mobile en même temps.
        val consultationId = infoRepository.getConsultationId(slugOrId) ?: return null

        return getConsultationDetails(consultationId = consultationId)?.let { details ->
            ConsultationDetailsV2WithInfo(
                consultation = details.consultation,
                update = details.update,
                feedbackStats = details.feedbackStats,
                history = details.history,
                participantCount = if (details.update.hasParticipationInfo || details.update.hasQuestionsInfo) {
                    getParticipantCount(consultationId)
                } else 0,
                isUserFeedbackPositive = null,
            )
        }
    }

    private fun getConsultationDetails(consultationId: String): ConsultationDetailsV2? {
        return infoRepository.getConsultation(consultationId)?.let { consultationInfo ->
            if (isConsultationOngoing(consultationInfo)) {
                getOngoingConsultationDetails(consultationInfo)
            } else {
                getLastConsultationDetails(consultationInfo)
            }
        } ?: run {
            cacheRepository.initUnansweredUsersConsultationDetails(consultationId, null)
            cacheRepository.initLastConsultationDetails(consultationId, null)
            null
        }
    }

    private fun isConsultationOngoing(consultationInfo: ConsultationInfo): Boolean {
        return LocalDateTime.now(clock).isBefore(consultationInfo.endDate)
    }

    private fun getOngoingConsultationDetails(
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
                    feedbackStats = null,
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
                    feedbackStats = null,
                    history = historyRepository.getConsultationUpdateHistory(consultationInfo.id),
                )
            }
        }.also { details ->
            cacheRepository.initLastConsultationDetails(consultationInfo.id, details)
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
