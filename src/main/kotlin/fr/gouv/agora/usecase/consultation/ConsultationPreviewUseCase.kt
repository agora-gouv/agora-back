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

        val ongoingConsultations = consultationInfoRepository.getOngoingConsultationsWithUnpublished(userTerritoires)
            .removeAnsweredConsultation(answeredConsultations)
            .sortedBy { it.endDate }
        val finishedConsultations = consultationInfoRepository.getFinishedConsultationsWithUnpublished(userTerritoires)

        if (authentificationHelper.canViewUnpublishedConsultations()) {
            return ConsultationPreviewPage(ongoingConsultations, finishedConsultations, answeredConsultations)
        }

        return ConsultationPreviewPage(
            ongoingConsultations.filter { it.isPublished },
            finishedConsultations.filter { it.isPublished },
            answeredConsultations
        )
    }

    private fun List<ConsultationPreview>.removeAnsweredConsultation(answeredList: List<ConsultationPreviewFinished>): List<ConsultationPreview> {
        return this.filterNot { ongoingConsultation ->
            answeredList.any { answeredConsultation -> answeredConsultation.id == ongoingConsultation.id }
        }
    }
}
