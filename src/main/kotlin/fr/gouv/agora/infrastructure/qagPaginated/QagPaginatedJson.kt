package fr.gouv.agora.infrastructure.qagPaginated

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.qagHome.HeaderJson
import fr.gouv.agora.infrastructure.qagHome.QagPreviewJson

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QagPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("qags")
    val qags: List<QagPreviewJson>,
)

data class QagPaginatedJsonV2(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("header")
    val header: HeaderJson?,
    @JsonProperty("qags")
    val qags: List<QagPreviewJson>,
)