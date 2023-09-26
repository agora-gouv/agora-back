package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewAnswered
import fr.social.gouv.agora.domain.ConsultationPreviewInfo
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewAnsweredMapper {

    fun toConsultationPreviewAnswered(
        consultationPreviewInfo: ConsultationPreviewInfo,
        consultationUpdate: ConsultationUpdate,
        thematique: Thematique,
    ): ConsultationPreviewAnswered {
        return ConsultationPreviewAnswered(
            id = consultationPreviewInfo.id,
            title = consultationPreviewInfo.title,
            coverUrl = consultationPreviewInfo.coverUrl,
            thematique = thematique,
            step = consultationUpdate.status,
        )
    }

    fun toConsultationPreviewAnswered(
        consultationInfo: ConsultationInfo,
        consultationUpdate: ConsultationUpdate,
        thematique: Thematique,
    ): ConsultationPreviewAnswered {
        return ConsultationPreviewAnswered(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            step = consultationUpdate.status,
        )
    }

}