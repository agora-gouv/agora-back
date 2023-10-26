package fr.gouv.agora.infrastructure.responseQagPaginated

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.qagHome.ResponseQagPreviewJson

data class ResponseQagPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("responses")
    val responses: List<ResponseQagPreviewJson>,
)