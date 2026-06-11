package fr.gouv.agora.infrastructure.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class StrapiDTO<T>(
    @JsonProperty("data")
    val data: List<T>,
    @JsonProperty("meta")
    val meta: StrapiMetadata,
) {
    companion object {
        fun <T> ofEmpty(): StrapiDTO<T> {
            return StrapiDTO(emptyList(), StrapiMetadata(StrapiMetaPagination(0, 0, 0, 0)))
        }
    }
}

data class StrapiSingleTypeDTO<T>(
    @JsonProperty("data")
    val data: T,
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

@JsonIgnoreProperties(ignoreUnknown = true)
data class StrapiMediaPicture(
    @JsonProperty("formats")
    val formats: StrapiMediaPictureFormats?,
    @JsonProperty("url")
    val pictureUrlNotOptimized: String,
) {
    fun mediaUrl(): String {
        return formats?.medium?.url ?: pictureUrlNotOptimized
    }
}

data class StrapiMediaPictureFormats(
    @JsonProperty("medium")
    val medium: StrapiMediaPictureFormatMedium?,
)

data class StrapiMediaPictureFormatMedium(
    @JsonProperty("url")
    val url: String,
)
