package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ConsultationPreviewFinishedMapper {
    fun toConsultationPreviewFinished(
        consultationInfo: ConsultationWithUpdateInfo,
        thematique: Thematique,
    ): ConsultationPreviewFinished {
        return ConsultationPreviewFinished(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            updateLabel = consultationInfo.updateLabel,
            endDate = consultationInfo.endDate,
            updateDate = consultationInfo.updateDate,
        )
    }
}
