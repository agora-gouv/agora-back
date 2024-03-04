package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class ConsultationPreviewOngoingMapper(private val clock: Clock) {

    companion object {
        private const val MAXIMUM_DAYS_DIFFERENCE_TO_HIGHLIGHT = 6L
    }

    fun toConsultationPreviewOngoing(
        consultationInfo: ConsultationInfo,
        thematique: Thematique,
    ): ConsultationPreviewOngoing {
        return ConsultationPreviewOngoing(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            thematique = thematique,
            endDate = consultationInfo.endDate,
            highlightLabel = buildHighlightLabel(consultationInfo.endDate),
        )
    }

    fun buildHighlightLabel(endDate: Date): String? {
        val dateNow = LocalDateTime.now(clock)
        val consultationEndDate = endDate.toLocalDateTime()
        val daysDifference = ChronoUnit.DAYS.between(LocalDateTime.now(clock), endDate.toLocalDateTime())

        return if (consultationEndDate.isAfter(dateNow)) {
            when (daysDifference) {
                0L -> "Dernier jour !"
                in 1L..MAXIMUM_DAYS_DIFFERENCE_TO_HIGHLIGHT -> "Plus que ${daysDifference + 1} jours !"
                else -> null
            }
        } else null
    }

}