package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.infrastructure.consultation.ConsultationPreviewJsonMapper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList
import org.springframework.stereotype.Component

@Component
class ConsultationPaginatedJsonMapper(
    private val consultationPreviewJsonMapper: ConsultationPreviewJsonMapper,
) {

    fun toJson(consultationFinishedPaginatedList: ConsultationFinishedPaginatedList): ConsultationPaginatedJson {
        return ConsultationPaginatedJson(
            maxPageNumber = consultationFinishedPaginatedList.maxPageNumber,
            consultations = consultationFinishedPaginatedList.consultationFinishedList.map { domain -> consultationPreviewJsonMapper.toJson(domain) },
        )
    }

    fun toJson(consultationAnsweredPaginatedList: ConsultationAnsweredPaginatedList): ConsultationPaginatedJson {
        return ConsultationPaginatedJson(
            maxPageNumber = consultationAnsweredPaginatedList.maxPageNumber,
            consultations = consultationAnsweredPaginatedList.consultationAnsweredList.map { domain -> consultationPreviewJsonMapper.toJson(domain) },
        )
    }

    fun toJson(consultations: List<ConsultationPreviewFinished>): ConsultationPaginatedJson {
        return ConsultationPaginatedJson(
            maxPageNumber = 1,
            consultations = consultations.map { domain -> consultationPreviewJsonMapper.toJson(domain) },
        )
    }
}
