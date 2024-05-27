package fr.gouv.agora.infrastructure.responseQag.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class StrapiResponseQagDTO(
    @JsonProperty("data")
    val data: List<StrapiModelAttributes>,
    @JsonProperty("meta")
    val meta: StrapiMetaInformations,
) {
    companion object {
        fun ofEmpty(): StrapiResponseQagDTO {
            return StrapiResponseQagDTO(emptyList(), StrapiMetaInformations(StrapiMetaPagination(0, 0, 0 , 0)))
        }
    }
}

data class StrapiModelAttributes(
    @JsonProperty(value = "attributes")
    val attributes: StrapiResponseQag
)

data class StrapiResponseQag(
    @JsonProperty(value = "auteur")
    val auteur: String,
    @JsonProperty(value = "auteurPortraitUrl")
    val auteurPortraitUrl: String,
    @JsonProperty(value = "reponseDate")
    val reponseDate: LocalDate,
    @JsonProperty(value = "feedbackQuestion")
    val feedbackQuestion: String,
    @JsonProperty(value = "questionId")
    val questionId: String,
    @JsonProperty("reponseType")
    val reponseType: List<StrapiResponseQagType>
)

data class StrapiResponseQagType(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("text")
    val text: String,
)

data class StrapiMetaInformations(
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
