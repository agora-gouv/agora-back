package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationDetailsV2UseCase(
    private val clock: Clock,
    private val infoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {

    fun getConsultation(consultationId: String, userId: String): ConsultationDetailsV2WithInfo? {
        return getConsultationDetails(consultationId, userId)?.let { details ->
            ConsultationDetailsV2WithInfo(
                consultation = details.consultation,
                thematique = details.thematique,
                update = details.update,
                history = details.history,
                participantCount = getParticipantCount(consultationId),
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
        val isConsultationOngoing = LocalDateTime.now(clock).isBefore(consultationInfo.endDate.toLocalDateTime())
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
                    history = emptyList(),
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
                // TODO : retrieve real history
                ConsultationDetailsV2(
                    consultation = consultationInfo,
                    thematique = thematique,
                    update = update,
                    history = listOf(
                        ConsultationUpdateHistory(
                            stepNumber = 1,
                            ConsultationUpdateHistoryType.UPDATE,
                            consultationUpdateId = "aa0180a2-fa10-4cdf-888b-bd58bb6f1709",
                            status = ConsultationUpdateHistoryStatus.DONE,
                            title = "Lancement",
                            updateDate = consultationInfo.startDate,
                            actionText = "Voir les objectifs",
                        ),
                        ConsultationUpdateHistory(
                            stepNumber = 2,
                            ConsultationUpdateHistoryType.RESULTS,
                            consultationUpdateId = "aa0180a2-fa10-4cdf-888b-bd58bb6f1709",
                            status = ConsultationUpdateHistoryStatus.CURRENT,
                            title = "Fin de la consultation",
                            updateDate = consultationInfo.endDate,
                            actionText = "Consulter toutes les réponses",
                        )
                    )
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