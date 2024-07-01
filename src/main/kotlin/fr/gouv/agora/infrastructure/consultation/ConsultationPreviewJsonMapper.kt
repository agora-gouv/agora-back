package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.ConsultationStatus.COLLECTING_DATA
import fr.gouv.agora.domain.ConsultationStatus.EXECUTION
import fr.gouv.agora.domain.ConsultationStatus.POLITICAL_COMMITMENT
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.infrastructure.utils.DateUtils
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ConsultationPreviewJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
    private val clock: Clock,
) {

    fun toJson(
        domainOngoingList: List<ConsultationPreviewOngoing>,
        domainFinishedList: List<ConsultationPreviewFinished>,
        domainAnsweredList: List<ConsultationPreviewFinished>,
    ): ConsultationPreviewJson {
        return ConsultationPreviewJson(
            ongoingList = domainOngoingList.map { domain ->
                ConsultationOngoingJson(
                    id = domain.id,
                    title = domain.title,
                    coverUrl = domain.coverUrl,
                    endDate = dateMapper.toFormattedDate(domain.endDate),
                    thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
                    highlightLabel = domain.highlightLabel(LocalDateTime.now(clock)),
                )
            },
            finishedList = domainFinishedList.map(::toJson),
            answeredList = domainAnsweredList.map(::toJson),
        )
    }

    private fun toJson(domain: ConsultationPreviewFinished): ConsultationFinishedJson {
        val now = LocalDateTime.now(clock)

        return ConsultationFinishedJson(
            id = domain.id,
            title = domain.title,
            coverUrl = domain.coverUrl,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            step = statusToJson(domain.getStep(now)),
            updateLabel = domain.getUpdateLabel(now),
            updateDate = dateMapper.toFormattedDate(domain.updateDate),
        )
    }

    private fun statusToJson(step: ConsultationStatus) = when (step) {
        COLLECTING_DATA -> 1
        POLITICAL_COMMITMENT -> 2
        EXECUTION -> 3
    }
}
