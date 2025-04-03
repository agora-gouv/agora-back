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

data class StrapiSingleTypeDTO <T>(
    @JsonProperty("data")
    val data: StrapiAttributes<T>,
)

data class StrapiDataList <T>(
    @JsonProperty("data")
    val data: List<StrapiAttributes<T>>,
)

data class StrapiData <T>(
    @JsonProperty("data")
    val data: StrapiAttributes<T>,
)

data class StrapiDataNullable <T>(
    @JsonProperty("data")
    val data: StrapiAttributes<T>?,
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

data class StrapiMediaVideo(
    @JsonProperty("url")
    val url: String,
)

data class StrapiMediaPdf(
    @JsonProperty("url")
    val url: String,
)

data class StrapiMediaPicture(
    @JsonProperty("formats")
    val formats: StrapiMediaPictureFormats,
)

data class StrapiMediaPictureFormats(
    @JsonProperty("medium")
    val medium: StrapiMediaPictureFormatMedium,
)

data class StrapiMediaPictureFormatMedium(
    @JsonProperty("url")
    val url: String,
    @JsonProperty("hash")
    val hash: String,
    @JsonProperty("width")
    val width: Int,
    @JsonProperty("height")
    val height: Int,
)
