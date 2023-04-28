package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationPreviewOngoingListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository,
) {
    fun getConsultationPreviewOngoingList(): List<ConsultationPreviewOngoing> {
        return consultationPreviewOngoingRepository.getConsultationPreviewOngoingList()
            .mapNotNull { consultationPreviewOngoingInfo ->
                thematiqueRepository.getThematiqueList()
                    .find { thematique -> thematique.id == consultationPreviewOngoingInfo.thematiqueId }
                    ?.let { thematique ->
                        ConsultationPreviewOngoing(
                            id = consultationPreviewOngoingInfo.id,
                            title = consultationPreviewOngoingInfo.title,
                            coverUrl = consultationPreviewOngoingInfo.coverUrl,
                            endDate = consultationPreviewOngoingInfo.endDate,
                            thematique = thematique,
                            hasAnswered = false, //TODO feat 99
                        )
                    }
            }
    }
}