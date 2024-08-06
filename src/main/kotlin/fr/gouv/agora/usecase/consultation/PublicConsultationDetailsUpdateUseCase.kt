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
        slugUpdate: String,
    ): ConsultationDetailsV2WithInfo? {
        // TODO soit je vais chercher les ids et je continue le flow et ça me permet de garder le cache
        // TODO soit je vais chercher l'objet via slug. Mais doublon côté cache

        // Pour le moment on récupère soit par slug, soit par ID, pour ne pas avoir à obliger la mise à jour
        // mobile en même temps.
        val consultationId = infoRepository.getConsultationId(slugOrIdConsultation) ?: return null
        val consultationUpdateId = updateRepository.getConsultationUpdateId(consultationId, slugUpdate) ?: return null

        val cacheResult = cacheRepository.getConsultationDetails(
            consultationId = consultationId,
            consultationUpdateId = consultationUpdateId,
        )

        // TODO refaire le cache à la fin du uc ?

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
                isUserFeedbackPositive = null,
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
