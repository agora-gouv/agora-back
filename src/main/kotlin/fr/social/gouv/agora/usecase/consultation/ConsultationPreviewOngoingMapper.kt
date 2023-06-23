package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
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
        hasAnswered: Boolean,
    ): ConsultationPreviewOngoing {


        return ConsultationPreviewOngoing(
            id = consultationPreviewOngoingInfo.id,
            title = consultationPreviewOngoingInfo.title,
            coverUrl = consultationPreviewOngoingInfo.coverUrl,
            thematique = thematique,
            endDate = consultationPreviewOngoingInfo.endDate,
            highlightLabel = buildHighlightLabel(consultationPreviewOngoingInfo.endDate),
            hasAnswered = hasAnswered,
        )
    }

    fun buildHighlightLabel(endDate: Date): String? {
        return when (val daysDifference = ChronoUnit.DAYS.between(LocalDate.now(clock), endDate.toLocalDate())) {
            1L -> "Dernier jour !"
            in 1L..MAXIMUM_DAYS_DIFFERENCE_TO_HIGHLIGHT -> "Plus que $daysDifference jours"
            else -> null
        }
    }

}