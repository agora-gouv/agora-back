package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationPreviewOngoingListUseCase(
    private val consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
) {
    fun getConsultationPreviewOngoingList(userId: String): List<ConsultationPreviewOngoing> {
        return consultationPreviewOngoingRepository.getConsultationPreviewOngoingList()
            .mapNotNull { consultationPreviewOngoingInfo ->
                thematiqueRepository.getThematique(thematiqueId = consultationPreviewOngoingInfo.thematiqueId)
                    ?.let { thematique ->
                        ConsultationPreviewOngoing(
                            id = consultationPreviewOngoingInfo.id,
                            title = consultationPreviewOngoingInfo.title,
                            coverUrl = consultationPreviewOngoingInfo.coverUrl,
                            endDate = consultationPreviewOngoingInfo.endDate,
                            thematique = thematique,
                            hasAnswered = hasUserAnsweredConsultation(consultationPreviewOngoingInfo, userId)
                        )
                    }
            }
    }

    private fun hasUserAnsweredConsultation(
        consultationPreviewOngoingInfo: ConsultationPreviewOngoingInfo,
        userId: String,
    ) = consultationResponseRepository.getConsultationResponses(consultationPreviewOngoingInfo.id)
        .any { consultationResponse -> consultationResponse.userId == userId }
}