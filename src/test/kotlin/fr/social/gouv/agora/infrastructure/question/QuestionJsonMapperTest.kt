package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class QuestionJsonMapperTest {

    @Autowired
    private lateinit var questionJsonMapper: QuestionJsonMapper

    private val choixPossibleDefault = ChoixPossibleDefault(
        id = "choixPossibleId",
        label = "label",
        ordre = 1,
        questionId = "questionId",
    )

    private val expectedChoixPossibleDefaultJson = ChoixPossibleJson(
        id = "choixPossibleId",
        label = "label",
        order = 1,
        nextQuestionId = null,
    )

    private val choixPossibleConditional = ChoixPossibleConditional(
        id = "choixPossibleId",
        label = "label",
        ordre = 1,
        questionId = "questionId",
        nextQuestionId = "nextQuestionId",
    )

    private val expectedChoixPossibleConditionalJson = ChoixPossibleJson(
        id = "choixPossibleId",
        label = "label",
        order = 1,
        nextQuestionId = "nextQuestionId",
    )

    private val questionUniqueChoice = QuestionUniqueChoice(
        id = "questionUniqueChoiceId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        consultationId = "consultationId",
        nextQuestionId = "nextQuestionId",
        choixPossibleList = listOf(choixPossibleDefault),
    )

    private val expectedQuestionUniqueChoiceJson = QuestionUniqueChoiceJson(
        id = "questionUniqueChoiceId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        questionProgress = "Question 1/1",
        nextQuestionId = "nextQuestionId",
        possibleChoices = listOf(expectedChoixPossibleDefaultJson),
    )

    private val questionChoixMultiple = QuestionMultipleChoices(
        id = "questionMultipleChoicesId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        consultationId = "consultationId",
        nextQuestionId = "nextQuestionId",
        choixPossibleList = listOf(choixPossibleDefault),
        maxChoices = 2,
    )

    private val expectedQuestionMultipleChoiceJson = QuestionMultipleChoicesJson(
        id = "questionMultipleChoicesId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        questionProgress = "Question 1/1",
        maxChoices = 2,
        nextQuestionId = "nextQuestionId",
        possibleChoices = listOf(expectedChoixPossibleDefaultJson),
    )

    private val questionChapter = QuestionChapter(
        id = "questionChapterId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        consultationId = "consultationId",
        nextQuestionId = "nextQuestionId",
        description = "description",
    )

    private val expectedQuestionChapterJson = QuestionChapterJson(
        id = "questionChapterId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        description = "description",
        nextQuestionId = "nextQuestionId",
    )

    private val questionOpen = QuestionOpen(
        id = "questionOpenId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        consultationId = "consultationId",
        nextQuestionId = "nextQuestionId",
    )

    private val expectedQuestionOpenJson = QuestionOpenedJson(
        id = "questionOpenId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        questionProgress = "Question 1/1",
        nextQuestionId = "nextQuestionId"
    )

    private val questionConditional = QuestionConditional(
        id = "questionConditionalId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        consultationId = "consultationId",
        nextQuestionId = "nextQuestionId",
        choixPossibleList = listOf(choixPossibleConditional),
    )

    private val expectedQuestionConditionalJson = QuestionConditionalJson(
        id = "questionConditionalId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        questionProgress = "Question 1/1",
        possibleChoices = listOf(expectedChoixPossibleConditionalJson),
    )

    @Test
    fun `toJson - when QuestionUniqueChoice - should return QuestionUniqueChoiceJson only`() {
        // When
        val result = questionJsonMapper.toJson(listOf(questionUniqueChoice))

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                chapters = emptyList(),
                questionsMultipleChoices = emptyList(),
                questionsOpened = emptyList(),
                questionsUniqueChoice = listOf(expectedQuestionUniqueChoiceJson),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson - when QuestionChoixMultiple - should return QuestionMultipleChoicesJson only`() {
        // Given
        val questionsMultipleChoice = listOf(questionChoixMultiple.copy(order = 1))

        // When
        val result = questionJsonMapper.toJson(questionsMultipleChoice)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                chapters = emptyList(),
                questionsUniqueChoice = emptyList(),
                questionsOpened = emptyList(),
                questionsMultipleChoices = listOf(expectedQuestionMultipleChoiceJson),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson - when QuestionOpen - should return QuestionOpenedJson only`() {
        // When
        val result = questionJsonMapper.toJson(listOf(questionOpen))

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionsMultipleChoices = emptyList(),
                questionsUniqueChoice = emptyList(),
                chapters = emptyList(),
                questionsOpened = listOf(expectedQuestionOpenJson),
                questionsWithCondition = listOf(),
            )
        )
    }

    @Test
    fun `toJson - when QuestionChapter - should return QuestionChapterJson only`() {
        // When
        val result = questionJsonMapper.toJson(listOf(questionChapter))

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionsMultipleChoices = emptyList(),
                questionsUniqueChoice = emptyList(),
                questionsOpened = emptyList(),
                chapters = listOf(expectedQuestionChapterJson),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson - when QuestionConditional - should return QuestionConditionalJson only`() {
        // When
        val result = questionJsonMapper.toJson(listOf(questionConditional))

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionsMultipleChoices = emptyList(),
                questionsUniqueChoice = emptyList(),
                chapters = emptyList(),
                questionsOpened = listOf(),
                questionsWithCondition = listOf(expectedQuestionConditionalJson),
            )
        )
    }

    @Test
    fun `toJson - should return the correct question Progress`() {
        // Given
        val questions = listOf(
            questionOpen.copy(order = 1),
            questionChapter.copy(order = 2),
            questionChoixMultiple.copy(order = 3),
            questionChapter.copy(order = 4),
            questionUniqueChoice.copy(order = 5),
        )

        // When
        val result = questionJsonMapper.toJson(questions)

        // Then
        assertThat(result.questionsOpened[0].questionProgress).isEqualTo("Question 1/3")
        assertThat(result.questionsMultipleChoices[0].questionProgress).isEqualTo("Question 2/3")
        assertThat(result.questionsUniqueChoice[0].questionProgress).isEqualTo("Question 3/3")
    }
}