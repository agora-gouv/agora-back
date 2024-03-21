package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.infrastructure.consultation.ConsultationFinishedJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList
import org.springframework.stereotype.Component

@Component
class ConsultationPaginatedJsonMapper(private val thematiqueJsonMapper: ThematiqueJsonMapper) {

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
        return ConsultationFinishedJson(
            id = domain.id,
            title = domain.title,
            coverUrl = domain.coverUrl,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            step = statusToJson(domain.step),
            updateLabel = domain.updateLabel,
        )
    }

    private fun statusToJson(step: ConsultationStatus) = when (step) {
        ConsultationStatus.COLLECTING_DATA -> 1
        ConsultationStatus.POLITICAL_COMMITMENT -> 2
        ConsultationStatus.EXECUTION -> 3
    }
}