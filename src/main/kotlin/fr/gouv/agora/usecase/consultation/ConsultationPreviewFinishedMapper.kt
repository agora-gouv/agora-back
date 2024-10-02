package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedMapper {
    fun toConsultationPreviewFinished(
        consultationsInfo: List<ConsultationWithUpdateInfo>,
        thematiques: List<Thematique>,
    ): List<ConsultationPreviewFinished> {

        return consultationsInfo.mapNotNull { consultationInfo ->
            val thematique = thematiques.firstOrNull { it.id == consultationInfo.thematiqueId }
                ?: return@mapNotNull null

            ConsultationPreviewFinished(
                id = consultationInfo.id,
                slug = consultationInfo.slug,
                title = consultationInfo.title,
                coverUrl = consultationInfo.coverUrl,
                thematique = thematique,
                updateLabel = consultationInfo.updateLabel,
                endDate = consultationInfo.endDate,
                lastUpdateDate = consultationInfo.updateDate,
                isPublished = true,
                territory = consultationInfo.territory,
            )
        }
    }
}
