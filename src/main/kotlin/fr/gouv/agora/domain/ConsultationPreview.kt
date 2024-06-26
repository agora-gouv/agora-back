package fr.gouv.agora.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ConsultationPreviewOngoing(
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
    val step: ConsultationStatus,
    val updateLabel: String?,
    val updateDate: LocalDate,
)
