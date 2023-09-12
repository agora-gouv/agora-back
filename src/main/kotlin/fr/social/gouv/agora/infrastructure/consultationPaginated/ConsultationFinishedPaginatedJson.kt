package fr.social.gouv.agora.infrastructure.consultationPaginated

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.consultation.ConsultationFinishedJson

data class ConsultationFinishedPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("consultations")
    val consultations: List<ConsultationFinishedJson>,
)