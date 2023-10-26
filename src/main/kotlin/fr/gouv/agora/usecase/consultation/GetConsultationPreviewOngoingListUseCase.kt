package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
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
                        val hasAnswered = consultationResponseRepository.hasAnsweredConsultation(
                            consultationId = consultationPreviewOngoingInfo.id,
                            userId = userId,
                        )
                        if (hasAnswered) {
                            null
                        } else {
                            mapper.toConsultationPreviewOngoing(
                                consultationPreviewOngoingInfo = consultationPreviewOngoingInfo,
                                thematique = thematique,
                            )
                        }
                    }
            }
    }
}

