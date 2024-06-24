package fr.gouv.agora.infrastructure.common

import com.fasterxml.jackson.annotation.JsonProperty

data class StrapiDTO <T>(
    @JsonProperty("data")
    val data: List<StrapiAttributes<T>>,
    @JsonProperty("meta")
    val meta: StrapiMetadata,
) {
    companion object {
        fun <T> ofEmpty(): StrapiDTO<T> {
            return StrapiDTO(emptyList(), StrapiMetadata(StrapiMetaPagination(0, 0, 0, 0)))
        }
    }
}

// TODO naming
data class StrapiDTOX <T>(
    @JsonProperty("data")
    val data: List<StrapiAttributes<T>>,
)
// TODO naming
data class StrapiDTOXX <T>(
    @JsonProperty("data")
    val data: StrapiAttributes<T>,
)

data class StrapiAttributes<T>(
    @JsonProperty(value = "attributes")
    val attributes: T,
    @JsonProperty(value = "id")
    val id: String
)

data class StrapiMetadata(
    @JsonProperty("pagination")
    val pagination: StrapiMetaPagination,
)

data class StrapiMetaPagination(
    @JsonProperty("page")
    val page: Int,
    @JsonProperty("pageSize")
    val pageSize: Int,
    @JsonProperty("pageCount")
    val pageCount: Int,
    @JsonProperty("total")
    val total: Int,
)
