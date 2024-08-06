package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.consultation.ConsultationFinishedJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPaginatedJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
    private val clock: Clock,
) {

    fun toJson(consultationFinishedPaginatedList: ConsultationFinishedPaginatedList): ConsultationPaginatedJson {
        return ConsultationPaginatedJson(
            maxPageNumber = consultationFinishedPaginatedList.maxPageNumber,
            consultations = consultationFinishedPaginatedList.consultationFinishedList.map { domain -> toJson(domain) },
        )
    }

    fun toJson(consultationAnsweredPaginatedList: ConsultationAnsweredPaginatedList): ConsultationPaginatedJson {
        return ConsultationPaginatedJson(
            maxPageNumber = consultationAnsweredPaginatedList.maxPageNumber,
            consultations = consultationAnsweredPaginatedList.consultationAnsweredList.map { domain -> toJson(domain) },
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
        ConsultationStatus.COLLECTING_DATA -> 1
        ConsultationStatus.POLITICAL_COMMITMENT -> 2
        ConsultationStatus.EXECUTION -> 3
    }
}
