package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewPage
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class ConsultationPreviewUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val authentificationHelper: AuthentificationHelper,
    private val profileRepository: ProfileRepository,
) {
    fun getConsultationPreviewPage(): ConsultationPreviewPage {
        val userId = authentificationHelper.getUserId()

        val profile = if (userId != null) {
             profileRepository.getProfile(userId)
        } else null
        val userTerritoires = Territoire.of(profile)

        val answeredConsultations = if (userId != null) {
            consultationInfoRepository.getAnsweredConsultations(userId)
        } else { emptyList() }

        val (ongoingConsultations, finishedConsultations) = if (authentificationHelper.canViewUnpublishedConsultations()) {
            Pair(
                consultationInfoRepository.getOngoingConsultationsWithUnpublished(userTerritoires),
                consultationInfoRepository.getFinishedConsultationsWithUnpublished(userTerritoires),
            )
        } else {
            Pair(
                consultationInfoRepository.getOngoingConsultations(userTerritoires),
                consultationInfoRepository.getFinishedConsultations(userTerritoires),
            )
        }

        return ConsultationPreviewPage(
            ongoingConsultations.removeAnsweredConsultation(answeredConsultations).sortedBy { it.endDate },
            finishedConsultations.sortedBy { it.lastUpdateDate },
            answeredConsultations
        )
    }

    private fun List<ConsultationPreview>.removeAnsweredConsultation(answeredList: List<ConsultationPreviewFinished>): List<ConsultationPreview> {
        return this.filterNot { ongoingConsultation ->
            answeredList.any { answeredConsultation -> answeredConsultation.id == ongoingConsultation.id }
        }
    }
}
