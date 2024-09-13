package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewOngoingMapper {
    fun toConsultationPreviewOngoing(
        consultationInfo: ConsultationInfo,
        thematique: Thematique,
    ): ConsultationPreview {
        return ConsultationPreview(
            id = consultationInfo.id,
            slug = consultationInfo.slug,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            endDate = consultationInfo.endDate,
            isPublished = true,
            territory = consultationInfo.territory,
        )
    }
}
