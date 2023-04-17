package fr.social.gouv.agora.infrastructure.question

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionsJson(
    @JsonProperty("questionsUniqueChoice")
    val questionsUniqueChoice: List<QuestionUniqueChoiceJson>,
    @JsonProperty("questionsOpened")
    val questionsOpened: List<QuestionOpenedJson>,
    @JsonProperty("questionsMultipleChoices")
    val questionsMultipleChoices: List<QuestionMultipleChoicesJson>,
    @JsonProperty("chapters")
    val chapters: List<ChapterJson>,
)

data class ChapterJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("consultationId")
    val consultationId: String,
    @JsonProperty("description")
    val description: String,
)

data class QuestionMultipleChoicesJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("consultationId")
    val consultationId: String,
    @JsonProperty("questionProgress")
    val questionProgress: String,
    @JsonProperty("possibleChoices")
    val possibleChoices: List<ChoixPossibleJson>,
    @JsonProperty("maxChoices")
    val maxChoices: Int,
)

data class QuestionOpenedJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("consultationId")
    val consultationId: String,
    @JsonProperty("questionProgress")
    val questionProgress: String,
)

data class QuestionUniqueChoiceJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("consultationId")
    val consultationId: String,
    @JsonProperty("questionProgress")
    val questionProgress: String,
    @JsonProperty("possibleChoices")
    val possibleChoices: List<ChoixPossibleJson>,
)

data class ChoixPossibleJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("order")
    val order: Int,
)