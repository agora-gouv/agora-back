package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedMapper {
    fun toConsultationPreviewFinished(
        consultationInfo: ConsultationWithUpdateInfo,
        thematique: Thematique,
    ): ConsultationPreviewFinished {
        return ConsultationPreviewFinished(
            id = consultationInfo.id,
            slug = consultationInfo.slug,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            updateLabel = consultationInfo.updateLabel,
            endDate = consultationInfo.endDate,
            lastUpdateDate = consultationInfo.updateDate,
            isPublished = true,
        )
    }
}
