package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewFinishedMapper(private val clock: Clock) {

    companion object {
        private const val MAX_UPDATE_LABEL_DURATION_IN_DAYS = 14L
    }

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
            step = ConsultationStatus.POLITICAL_COMMITMENT,
            updateLabel = null, // TODO
        )
    }

    fun toConsultationPreviewFinished(
        consultationInfo: ConsultationWithUpdateInfo,
        thematique: Thematique,
    ): ConsultationPreviewFinished {
        return ConsultationPreviewFinished(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            step = ConsultationStatus.POLITICAL_COMMITMENT,
            updateLabel = buildUpdateLabel(consultationInfo),
        )
    }

    fun toConsultationPreviewAnswered(
        consultationInfo: ConsultationWithUpdateInfo,
        thematique: Thematique,
    ): ConsultationPreviewAnswered {
        return ConsultationPreviewAnswered(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            step = ConsultationStatus.POLITICAL_COMMITMENT,
            updateLabel = buildUpdateLabel(consultationInfo),
        )
    }

    private fun buildUpdateLabel(consultationInfo: ConsultationWithUpdateInfo): String? {
        return consultationInfo.updateLabel?.let { updateLabel ->
            val dateNow = LocalDateTime.now(clock)
            val updateDate = consultationInfo.updateDate.toLocalDateTime()
            val maxUpdateDateLabel = updateDate.plusDays(MAX_UPDATE_LABEL_DURATION_IN_DAYS)
            if (updateDate == dateNow
                || maxUpdateDateLabel == dateNow
                || (updateDate.isBefore(dateNow) && dateNow.isBefore(maxUpdateDateLabel))
            ) {
                updateLabel
            } else null
        }
    }

}