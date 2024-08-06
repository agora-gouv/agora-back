package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.ConsultationStatus.COLLECTING_DATA
import fr.gouv.agora.domain.ConsultationStatus.EXECUTION
import fr.gouv.agora.domain.ConsultationStatus.POLITICAL_COMMITMENT
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
    private val clock: Clock,
) {

    fun toJson(
        domainOngoingList: List<ConsultationPreview>,
        domainFinishedList: List<ConsultationPreviewFinished>,
        domainAnsweredList: List<ConsultationPreviewFinished>,
    ): ConsultationPreviewJson {
        return ConsultationPreviewJson(
            ongoingList = domainOngoingList.map { domain ->
                ConsultationOngoingJson(
                    id = domain.id,
                    slug = domain.slug,
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
            slug = domain.slug,
            title = domain.title,
            coverUrl = domain.coverUrl,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            step = statusToJson(domain.getStep(now)),
            updateLabel = domain.getUpdateLabel(now),
            updateDate = dateMapper.toFormattedDate(domain.lastUpdateDate),
        )
    }

    private fun statusToJson(step: ConsultationStatus) = when (step) {
        COLLECTING_DATA -> 1
        POLITICAL_COMMITMENT -> 2
        EXECUTION -> 3
    }
}
