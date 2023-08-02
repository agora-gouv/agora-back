package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewAnswered
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationPreviewAnsweredListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository,
    private val consultationUpdateRepository: ConsultationUpdateRepository,
) {
    fun getConsultationPreviewAnsweredList(userId: String): List<ConsultationPreviewAnswered> {
        return consultationPreviewAnsweredRepository.getConsultationAnsweredList(userId = userId)
            .mapNotNull { consultationPreviewAnsweredInfo ->

                thematiqueRepository.getThematique(consultationPreviewAnsweredInfo.thematiqueId)
                    ?.let { thematique ->
                        consultationUpdateRepository.getConsultationUpdate(consultationPreviewAnsweredInfo.id)?.status?.let {
                            ConsultationPreviewAnswered(
                                id = consultationPreviewAnsweredInfo.id,
                                title = consultationPreviewAnsweredInfo.title,
                                coverUrl = consultationPreviewAnsweredInfo.coverUrl,
                                thematique = thematique,
                                step = it
                            )
                        }
                    }
            }
    }
}