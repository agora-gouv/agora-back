package fr.gouv.agora.infrastructure.responseQag.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.agora.infrastructure.common.StrapiDataNullable
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiMediaVideo
import fr.gouv.agora.infrastructure.common.StrapiRichText
import fr.gouv.agora.infrastructure.common.StrapiSingleTypeDTO
import java.time.LocalDate

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
    val reponseType: List<StrapiResponseQagType>,
    @JsonProperty("auteurPortrait")
    val auteurPortrait: StrapiDataNullable<StrapiMediaPicture>,
) {
    fun getAuthorPortraitUrl(): String {
        return if (auteurPortrait.data == null) auteurPortraitUrl
        else System.getenv("CMS_URL") + auteurPortrait.data.attributes.formats.medium.url
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "__component",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = StrapiResponseQagText::class, name = "reponse.reponsetextuelle"),
    JsonSubTypes.Type(value = StrapiResponseQagVideo::class, name = "reponse.reponse-video")
)
sealed interface StrapiResponseQagType

data class StrapiResponseQagText(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("text")
    val text: List<StrapiRichText>,
) : StrapiResponseQagType

data class StrapiResponseQagVideo(
    @JsonProperty("auteurDescription")
    val auteurDescription: String,
    @JsonProperty("urlVideo")
    val urlVideo: String,
    @JsonProperty("videoWidth")
    val videoWidth: Int,
    @JsonProperty("videoHeight")
    val videoHeight: Int,
    @JsonProperty("transcription")
    val transcription: String,
    @JsonProperty("informationAdditionnelleTitre")
    val informationAdditionnelleTitre: String?,
    @JsonProperty("page_title")
    val pageTitle: String,
    @JsonProperty("informationAdditionnelleDescription")
    val informationAdditionnelleDescription: List<StrapiRichText>?,
    @JsonProperty("video")
    val video: StrapiDataNullable<StrapiMediaVideo>,
) : StrapiResponseQagType {
    fun hasInformationAdditionnelle(): Boolean {
        return !informationAdditionnelleTitre.isNullOrBlank() && !informationAdditionnelleDescription.isNullOrEmpty()
    }

    fun getVideoUrl(): String {
        return if (video.data == null) urlVideo
        else System.getenv("CMS_URL") + video.data.attributes.url
    }
}
