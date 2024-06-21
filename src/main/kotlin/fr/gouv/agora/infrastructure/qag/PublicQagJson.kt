package fr.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublicQagJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("date")
    val date: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("supportCount")
    val supportCount: Int,
    @JsonProperty("response")
    val videoResponse: PublicQagResponseVideoJson?,
    @JsonProperty("textResponse")
    val textResponse: PublicQagResponseTextJson?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublicQagResponseVideoJson(
    @JsonProperty("author")
    val author: String,
    @JsonProperty("authorDescription")
    val authorDescription: String,
    @JsonProperty("authorPortraitUrl")
    val authorPortraitUrl: String,
    @JsonProperty("responseDate")
    val responseDate: String,
    @JsonProperty("videoUrl")
    val videoUrl: String,
    @JsonProperty("videoWidth")
    val videoWidth: Int,
    @JsonProperty("videoHeight")
    val videoHeight: Int,
    @JsonProperty("transcription")
    val transcription: String,
    @JsonProperty("additionalInfo")
    val additionalInfo: PublicQagResponseVideoAdditionalInfoJson?,
    @JsonProperty("feedbackQuestion")
    val feedbackQuestion: String,
)

data class PublicQagResponseVideoAdditionalInfoJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
)

data class PublicQagResponseTextJson(
    @JsonProperty("responseLabel")
    val responseLabel: String,
    @JsonProperty("responseText")
    val responseText: String,
    @JsonProperty("feedbackQuestion")
    val feedbackQuestion: String,
)
