package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewOngoingMapper {
    fun toConsultationPreviewOngoing(
        consultationInfo: ConsultationInfo,
        thematique: Thematique,
    ): ConsultationPreviewOngoing {
        return ConsultationPreviewOngoing(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            endDate = consultationInfo.endDate.toLocalDate(),
        )
    }
}
