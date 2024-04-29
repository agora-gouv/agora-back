package fr.gouv.agora.infrastructure.consultationResults

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ConsultationResultsJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("participantCount")
    val participantCount: Int,
    @JsonProperty("resultsUniqueChoice")
    val resultsUniqueChoice: List<QuestionResultsJson>,
    @JsonProperty("resultsMultipleChoice")
    val resultsMultipleChoice: List<QuestionResultsJson>,
    @JsonProperty("resultsOpen")
    val resultsOpen: List<QuestionOpenResultsJson>,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConsultationResultsWithUpdateJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("participantCount")
    val participantCount: Int,
    @JsonProperty("resultsUniqueChoice")
    val resultsUniqueChoice: List<QuestionResultsJson>,
    @JsonProperty("resultsMultipleChoice")
    val resultsMultipleChoice: List<QuestionResultsJson>,
    @JsonProperty("etEnsuite")
    val lastUpdate: ConsultationUpdatesJson,
    @JsonProperty("presentation")
    val presentation: PresentationJson,
)

data class QuestionResultsJson(
    @JsonProperty("questionId")
    val questionId: String,
    @JsonProperty("questionTitle")
    val questionTitle: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("responses")
    val responses: List<ChoiceResultsJson>,
)

data class QuestionOpenResultsJson(
    @JsonProperty("questionId")
    val questionId: String,
    @JsonProperty("questionTitle")
    val questionTitle: String,
)

data class ChoiceResultsJson(
    @JsonProperty("choiceId")
    val choiceId: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ratio")
    val ratio: Int,
)

data class ConsultationUpdatesJson(
    @JsonProperty("step")
    val step: Int,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("explanationsTitle")
    val explanationsTitle: String?,
    @JsonProperty("explanations")
    val explanations: List<ExplanationJson>?,
    @JsonProperty("video")
    val video: VideoJson?,
    @JsonProperty("conclusion")
    val conclusion: ConclusionJson?,
)

data class ExplanationJson(
    @JsonProperty("isTogglable")
    val isTogglable: Boolean,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("intro")
    val intro: String,
    @JsonProperty("imageUrl")
    val imageUrl: String,
    @JsonProperty("image")
    val image: ImageJson?,
    @JsonProperty("description")
    val description: String,
)

data class ImageJson(
    @JsonProperty("url")
    val url: String,
    @JsonProperty("description")
    val description: String?,
)

data class VideoJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("intro")
    val intro: String,
    @JsonProperty("videoUrl")
    val videoUrl: String,
    @JsonProperty("videoWidth")
    val videoWidth: Int,
    @JsonProperty("videoHeight")
    val videoHeight: Int,
    @JsonProperty("transcription")
    val transcription: String,
)

data class ConclusionJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
)

data class PresentationJson(
    @JsonProperty("startDate")
    val startDate: String,
    @JsonProperty("endDate")
    val endDate: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("tipsDescription")
    val tipsDescription: String,
)