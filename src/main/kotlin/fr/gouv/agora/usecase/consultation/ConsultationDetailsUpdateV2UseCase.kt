package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsUpdateV2UseCase(
    private val infoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val updateRepository: ConsultationUpdateV2Repository,
    private val userAnsweredRepository: UserAnsweredConsultationRepository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {

    fun getConsultationDetailsUpdate(
        consultationId: String,
        consultationUpdateId: String,
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
                thematique = details.thematique,
                update = details.update,
                history = details.history,
                participantCount = if (details.update.hasParticipationInfo || details.update.hasQuestionsInfo) {
                    getParticipantCount(consultationId)
                } else 0,
            )
        }
    }

    private fun buildConsultationDetails(consultationId: String, consultationUpdateId: String): ConsultationDetailsV2? {
        return infoRepository.getConsultation(consultationId)?.let { consultationInfo ->
            thematiqueRepository.getThematique(consultationInfo.thematiqueId)?.let { thematique ->
                updateRepository.getConsultationUpdate(consultationUpdateId = consultationUpdateId)?.let { update ->
                    ConsultationDetailsV2(
                        consultation = consultationInfo,
                        thematique = thematique,
                        update = update,
                        history = null,
                    )
                }
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