package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewFinished
import fr.social.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.domain.Thematique
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedMapper {

    fun toConsultationPreviewFinished(
        consultationPreviewFinishedInfo: ConsultationPreviewFinishedInfo,
        consultationUpdate: ConsultationUpdate,
        thematique: Thematique,
    ): ConsultationPreviewFinished {
        return ConsultationPreviewFinished(
            id = consultationPreviewFinishedInfo.id,
            title = consultationPreviewFinishedInfo.title,
            coverUrl = consultationPreviewFinishedInfo.coverUrl,
            thematique = thematique,
            step = consultationUpdate.status,
        )
    }

}