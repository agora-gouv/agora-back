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
class ConsultationPreviewFinishedMapper(private val clock: Clock) {

    companion object {
        private const val MAX_UPDATE_LABEL_DURATION_IN_DAYS = 90L
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
            updateLabel = buildUpdateLabel(consultationInfo),
            endDate = consultationInfo.endDate,
            updateDate = consultationInfo.updateDate,
        )
    }

    private fun buildUpdateLabel(consultationInfo: ConsultationWithUpdateInfo): String? {
        return consultationInfo.updateLabel?.let { updateLabel ->
            val dateNow = LocalDateTime.now(clock)
            val updateDate = consultationInfo.updateDate
            val maxUpdateDateLabel = updateDate.plusDays(MAX_UPDATE_LABEL_DURATION_IN_DAYS)
            if (dateNow.isBefore(updateDate) || dateNow.isAfter(maxUpdateDateLabel)) return null

            return updateLabel
        }
    }

}
