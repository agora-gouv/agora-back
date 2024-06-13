package fr.gouv.agora.infrastructure.responseQag.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
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
    val reponseType: List<StrapiResponseQagType>
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "__component", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = StrapiResponseQagText::class, name = "reponse.reponsetextuelle"),
    JsonSubTypes.Type(value = StrapiResponseQagVideo::class, name = "reponse.reponse-video")
)
sealed interface StrapiResponseQagType

data class StrapiResponseQagText(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("text")
    val text: List<StrapiRichText>?,
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
    @JsonProperty("informationAdditionnelleDescription")
    val informationAdditionnelleDescription: List<StrapiRichText>?,
) : StrapiResponseQagType

data class StrapiRichText(
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("bold")
    val bold: Boolean?,
    @JsonProperty("underline")
    val underline: Boolean?,
    @JsonProperty("strikethrough")
    val strikethrough: Boolean?,
    @JsonProperty("children")
    val children: List<StrapiRichText>?,
    @JsonProperty("format")
    val format: String?,
    @JsonProperty("code")
    val code: Boolean?,
    @JsonProperty("level")
    val level: Int?,
)
