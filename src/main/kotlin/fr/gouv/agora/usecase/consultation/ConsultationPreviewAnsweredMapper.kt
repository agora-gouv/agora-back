package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewAnswered
import fr.gouv.agora.domain.ConsultationUpdate
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewAnsweredMapper {

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