package fr.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QagJson(
    @JsonProperty("id")
    val id: String,
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
    @JsonProperty("canShare")
    val canShare: Boolean,
    @JsonProperty("canSupport")
    val canSupport: Boolean,
    @JsonProperty("canDelete")
    val canDelete: Boolean,
    @JsonProperty("support")
    val support: SupportQagJson?,
    @JsonProperty("isAuthor")
    val isAuthor: Boolean,
    @JsonProperty("response")
    val response: ResponseQagVideoJson?,
    @JsonProperty("textResponse")
    val textResponse: ResponseQagTextJson?,
)

data class SupportQagJson(
    @JsonProperty("count")
    val supportCount: Int,
    @JsonProperty("isSupported")
    val isSupportedByUser: Boolean,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseQagVideoJson(
    @JsonProperty("author")
    val author: String,
    @JsonProperty("authorDescription")
    val authorDescription: String,
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
    @JsonProperty("feedbackQuestion")
    val feedbackQuestion: String,
    @JsonProperty("feedbackStatus")
    val feedbackStatus: Boolean,
    @JsonProperty("feedbackResults")
    val feedbackResults: FeedbackResultsJson?,
    @JsonProperty("additionalInfo")
    val additionalInfo: AdditionalInfoJson?,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseQagTextJson(
    @JsonProperty("responseLabel")
    val responseLabel: String,
    @JsonProperty("responseText")
    val responseText: String,
    @JsonProperty("feedbackQuestion")
    val feedbackQuestion: String,
    @JsonProperty("feedbackStatus")
    val feedbackStatus: Boolean,
    @JsonProperty("feedbackResults")
    val feedbackResults: FeedbackResultsJson?,
)

data class FeedbackResultsJson(
    @JsonProperty("positiveRatio")
    val positiveRatio: Int,
    @JsonProperty("negativeRatio")
    val negativeRatio: Int,
    @JsonProperty("count")
    val count: Int,
)

data class AdditionalInfoJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
)
