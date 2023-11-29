package fr.gouv.agora.infrastructure.qagPaginated

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.qagHome.HeaderQagJson
import fr.gouv.agora.infrastructure.qagHome.QagPreviewJson

data class QagPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("qags")
    val qags: List<QagPreviewJson>,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QagPaginatedJsonV2(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("header")
    val header: HeaderQagJson?,
    @JsonProperty("qags")
    val qags: List<QagPreviewJson>,
)