package fr.gouv.agora.infrastructure.consultationPaginated

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.consultation.ConsultationFinishedJson

data class ConsultationPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("consultations")
    val consultations: List<ConsultationFinishedJson>,
)