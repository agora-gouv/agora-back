package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewFinishedMapper(private val clock: Clock) {

    companion object {
        private const val MAX_UPDATE_LABEL_DURATION_IN_DAYS = 30L
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
            step = if (LocalDateTime.now(clock).isBefore(consultationInfo.endDate.toLocalDateTime())) {
                ConsultationStatus.COLLECTING_DATA
            } else {
                ConsultationStatus.POLITICAL_COMMITMENT
            },
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