package fr.social.gouv.agora.infrastructure.responseQagPaginated

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.qagHome.ResponseQagPreviewJson

data class ResponseQagPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("responses")
    val responses: List<ResponseQagPreviewJson>,
)