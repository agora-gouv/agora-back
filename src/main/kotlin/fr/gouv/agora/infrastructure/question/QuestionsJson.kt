package fr.gouv.agora.infrastructure.question

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionsJson(
    @JsonProperty("questionCount")
    val questionCount: Int,
    @JsonProperty("questionsUniqueChoice")
    val questionsUniqueChoice: List<QuestionUniqueChoiceJson>,
    @JsonProperty("questionsOpened")
    val questionsOpened: List<QuestionOpenedJson>,
    @JsonProperty("questionsMultipleChoices")
    val questionsMultipleChoices: List<QuestionMultipleChoicesJson>,
    @JsonProperty("chapters")
    val chapters: List<QuestionChapterJson>,
    @JsonProperty("questionsWithCondition")
    val questionsWithCondition: List<QuestionConditionalJson>,
)

data class QuestionUniqueChoiceJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("popupDescription")
    val popupDescription: String?,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("questionProgress")
    val questionProgress: String,
    @JsonProperty("questionProgressA11y")
    val questionProgressDescription: String,
    @JsonProperty("nextQuestionId")
    val nextQuestionId: String?,
    @JsonProperty("possibleChoices")
    val possibleChoices: List<ChoixPossibleJson>,
)

data class QuestionMultipleChoicesJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("popupDescription")
    val popupDescription: String?,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("questionProgress")
    val questionProgress: String,
    @JsonProperty("questionProgressA11y")
    val questionProgressDescription: String,
    @JsonProperty("maxChoices")
    val maxChoices: Int,
    @JsonProperty("nextQuestionId")
    val nextQuestionId: String?,
    @JsonProperty("possibleChoices")
    val possibleChoices: List<ChoixPossibleJson>,
)

data class QuestionOpenedJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("popupDescription")
    val popupDescription: String?,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("questionProgress")
    val questionProgress: String,
    @JsonProperty("questionProgressA11y")
    val questionProgressDescription: String,
    @JsonProperty("nextQuestionId")
    val nextQuestionId: String?,
)

data class QuestionChapterJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("popupDescription")
    val popupDescription: String?,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("nextQuestionId")
    val nextQuestionId: String?,
    @JsonProperty("imageUrl")
    val imageUrl: String?,
)

data class QuestionConditionalJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("popupDescription")
    val popupDescription: String?,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("questionProgress")
    val questionProgress: String,
    @JsonProperty("questionProgressA11y")
    val questionProgressDescription: String,
    @JsonProperty("possibleChoices")
    val possibleChoices: List<ChoixPossibleJson>,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ChoixPossibleJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("hasOpenTextField")
    val hasOpenTextField: Boolean,
    @JsonProperty("nextQuestionId")
    val nextQuestionId: String?,
)
