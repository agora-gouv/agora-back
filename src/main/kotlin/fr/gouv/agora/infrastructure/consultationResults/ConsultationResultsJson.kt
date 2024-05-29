package fr.gouv.agora.infrastructure.consultationResults

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

data class QuestionResultsJson(
    @JsonProperty("questionId")
    val questionId: String,
    @JsonProperty("questionTitle")
    val questionTitle: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("seenRatio")
    val seenRatio: Int,
    @JsonProperty("responses")
    val responses: List<ChoiceResultsJson>,
)

data class QuestionOpenResultsJson(
    @JsonProperty("questionId")
    val questionId: String,
    @JsonProperty("questionTitle")
    val questionTitle: String,
    @JsonProperty("order")
    val order: Int,
)

data class ChoiceResultsJson(
    @JsonProperty("choiceId")
    val choiceId: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ratio")
    val ratio: Int,
)
