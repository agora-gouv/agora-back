package fr.social.gouv.agora.infrastructure.consultationPaginated

import fr.social.gouv.agora.domain.ConsultationPreviewFinished
import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.infrastructure.consultation.ConsultationFinishedJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.social.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList
import org.springframework.stereotype.Component

@Component
class ConsultationFinishedPaginatedJsonMapper(private val thematiqueJsonMapper: ThematiqueJsonMapper) {

    fun toJson(consultationFinishedPaginatedList: ConsultationFinishedPaginatedList): ConsultationFinishedPaginatedJson {
        return ConsultationFinishedPaginatedJson(
            maxPageNumber = consultationFinishedPaginatedList.maxPageNumber,
            consultations = consultationFinishedPaginatedList.consultationFinishedList.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: ConsultationPreviewFinished): ConsultationFinishedJson {
        return ConsultationFinishedJson(
            id = domain.id,
            title = domain.title,
            coverUrl = domain.coverUrl,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            step = statusToJson(domain.step)
        )
    }

    private fun statusToJson(step: ConsultationStatus) = when (step) {
        ConsultationStatus.COLLECTING_DATA -> 1
        ConsultationStatus.POLITICAL_COMMITMENT -> 2
        ConsultationStatus.EXECUTION -> 3
    }
}