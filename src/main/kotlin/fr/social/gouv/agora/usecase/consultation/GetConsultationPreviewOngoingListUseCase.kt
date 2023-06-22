package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationPreviewOngoingListUseCase(
    private val consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val mapper: ConsultationPreviewOngoingMapper,
) {
    fun getConsultationPreviewOngoingList(userId: String): List<ConsultationPreviewOngoing> {
        return consultationPreviewOngoingRepository.getConsultationPreviewOngoingList()
            .mapNotNull { consultationPreviewOngoingInfo ->
                thematiqueRepository.getThematique(thematiqueId = consultationPreviewOngoingInfo.thematiqueId)
                    ?.let { thematique ->
                        mapper.toConsultationPreviewOngoing(
                            consultationPreviewOngoingInfo = consultationPreviewOngoingInfo,
                            thematique = thematique,
                            hasAnswered = consultationResponseRepository.hasAnsweredConsultation(
                                consultationId = consultationPreviewOngoingInfo.id,
                                userId = userId,
                            ),
                        )
                    }
            }
    }

}