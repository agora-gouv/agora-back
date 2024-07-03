package fr.gouv.agora.domain

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ConsultationPreview(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematique: Thematique,
    val endDate: LocalDateTime,
) {
    fun highlightLabel(today: LocalDateTime): String? {
        val daysDifference = ChronoUnit.DAYS.between(today, endDate)

        if (endDate.isBefore(today)) {
            return null
        }

        return when (daysDifference) {
            0L -> "Dernier jour !"
            in 1L..6 -> "Plus que ${daysDifference + 1} jours !"
            else -> null
        }
    }
}

data class ConsultationPreviewFinished(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematique: Thematique,
    private val updateLabel: String?,
    val updateDate: LocalDateTime,
    val endDate: LocalDateTime,
) {
    fun getStep(now: LocalDateTime): ConsultationStatus {
        return if (now.isBefore(this.endDate)) {
            ConsultationStatus.COLLECTING_DATA
        } else {
            ConsultationStatus.POLITICAL_COMMITMENT
        }
    }

    fun getUpdateLabel(now: LocalDateTime): String? {
        if (updateLabel == null) return null

        val maxUpdateDateLabel = updateDate.plusDays(90)

        if (now.isBefore(updateDate) || now.isAfter(maxUpdateDateLabel)) return null

        return updateLabel
    }
}
