package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class ConsultationPreviewOngoingMapper(private val clock: Clock) {

    companion object {
        private const val MAXIMUM_DAYS_DIFFERENCE_TO_HIGHLIGHT = 15L
    }

    fun toConsultationPreviewOngoing(
        consultationPreviewOngoingInfo: ConsultationPreviewOngoingInfo,
        thematique: Thematique,
    ): ConsultationPreviewOngoing {
        return ConsultationPreviewOngoing(
            id = consultationPreviewOngoingInfo.id,
            title = consultationPreviewOngoingInfo.title,
            coverUrl = consultationPreviewOngoingInfo.coverUrl,
            thematique = thematique,
            endDate = consultationPreviewOngoingInfo.endDate,
            highlightLabel = buildHighlightLabel(consultationPreviewOngoingInfo.endDate),
        )
    }


    fun buildHighlightLabel(endDate: Date): String? {
        val dateNow = LocalDateTime.now(clock)
        val consultationEndDate = endDate.toLocalDateTime()
        val daysDifference = ChronoUnit.DAYS.between(LocalDateTime.now(clock), endDate.toLocalDateTime())

        return if (consultationEndDate.isAfter(dateNow)) {
            when (daysDifference) {
                0L -> "Dernier jour !"
                1L -> "Plus que 1 jour !"
                in 1L..MAXIMUM_DAYS_DIFFERENCE_TO_HIGHLIGHT -> "Plus que $daysDifference jours !"
                else -> null
            }
        } else null
    }

}