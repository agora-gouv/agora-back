package fr.gouv.agora.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

data class ConsultationPreviewOngoing(
    val id: String,
    val title: String,
    val coverUrl: String,
    val thematique: Thematique,
    val endDate: LocalDate,
) {
    fun highlightLabel(today: LocalDate): String? {
        val consultationEndDate = endDate.atStartOfDay()
        val daysDifference = ChronoUnit.DAYS.between(today, endDate)

        return if (consultationEndDate.isAfter(today.atStartOfDay())) {
            when (daysDifference) {
                0L -> "Dernier jour !"
                in 1L..6 -> "Plus que ${daysDifference + 1} jours !"
                else -> null
            }
        } else null
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
