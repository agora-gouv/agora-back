package fr.gouv.agora.infrastructure.qagPaginated

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.qagHome.QagPreviewJson

data class QagPaginatedJson(
    @JsonProperty("maxPageNumber")
    val maxPageNumber: Int,
    @JsonProperty("qags")
    val qags: List<QagPreviewJson>,
)