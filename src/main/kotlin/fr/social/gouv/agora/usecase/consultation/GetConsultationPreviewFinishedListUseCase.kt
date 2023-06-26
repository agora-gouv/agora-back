package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewFinished
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationPreviewFinishedListUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
    private val consultationUpdateRepository: ConsultationUpdateRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
) {

    fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished> {
        return consultationPreviewFinishedRepository.getConsultationFinishedList()
            .mapNotNull { consultationInfo ->
                consultationUpdateRepository.getConsultationUpdate(consultationId = consultationInfo.id)
                    ?.let { consultationUpdate ->
                        thematiqueRepository.getThematique(thematiqueId = consultationInfo.thematiqueId)
                            ?.let { thematique ->
                                mapper.toConsultationPreviewFinished(
                                    consultationPreviewFinishedInfo = consultationInfo,
                                    consultationUpdate = consultationUpdate,
                                    thematique = thematique,
                                )
                            }
                    }

            }
    }

}