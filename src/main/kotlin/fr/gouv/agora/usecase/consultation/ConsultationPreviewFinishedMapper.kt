package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewInfo
import fr.gouv.agora.domain.ConsultationUpdate
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedMapper {

    fun toConsultationPreviewFinished(
        consultationPreviewInfo: ConsultationPreviewInfo,
        consultationUpdate: ConsultationUpdate,
        thematique: Thematique,
    ): ConsultationPreviewFinished {
        return ConsultationPreviewFinished(
            id = consultationPreviewInfo.id,
            title = consultationPreviewInfo.title,
            coverUrl = consultationPreviewInfo.coverUrl,
            thematique = thematique,
            step = consultationUpdate.status,
        )
    }

    fun toConsultationPreviewFinished(
        consultationInfo: ConsultationInfo,
        consultationUpdate: ConsultationUpdate,
        thematique: Thematique,
    ): ConsultationPreviewFinished {
        return ConsultationPreviewFinished(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            step = consultationUpdate.status,
        )
    }

}