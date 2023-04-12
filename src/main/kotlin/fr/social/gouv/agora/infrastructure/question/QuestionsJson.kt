package fr.social.gouv.agora.infrastructure.question

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionsJson(
    @JsonProperty("questions")
    val questions: List<QuestionJson>,
)

data class QuestionJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("possible_choices")
    val possible_choices: List<ChoixPossibleJson>,
)

data class ChoixPossibleJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("order")
    val order: Int,
)