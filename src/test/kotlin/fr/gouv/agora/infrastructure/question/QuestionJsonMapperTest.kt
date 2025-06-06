package fr.gouv.agora.infrastructure.question

import fr.gouv.agora.domain.ChoixPossibleConditional
import fr.gouv.agora.domain.ChoixPossibleDefault
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.domain.Questions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class QuestionJsonMapperTest {

    @InjectMocks
    private lateinit var questionJsonMapper: QuestionJsonMapper

    private val choixPossibleDefault = ChoixPossibleDefault(
        id = "choixPossibleId",
        label = "label",
        ordre = 1,
        questionId = "questionId",
        hasOpenTextField = false,
    )

    private val expectedChoixPossibleDefaultJson = ChoixPossibleJson(
        id = "choixPossibleId",
        label = "label",
        order = 1,
        hasOpenTextField = false,
        nextQuestionId = null,
    )

    private val choixPossibleConditional = ChoixPossibleConditional(
        id = "choixPossibleId",
        label = "label",
        ordre = 1,
        questionId = "questionId",
        nextQuestionId = "nextQuestionId",
        hasOpenTextField = false,
    )

    private val expectedChoixPossibleConditionalJson = ChoixPossibleJson(
        id = "choixPossibleId",
        label = "label",
        order = 1,
        hasOpenTextField = false,
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
        questionProgressDescription = "Question 1 sur 1",
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
        questionProgressDescription = "Question 1 sur 1",
        maxChoices = 2,
        nextQuestionId = "nextQuestionId",
        possibleChoices = listOf(expectedChoixPossibleDefaultJson),
    )

    private val questionChapter = QuestionChapter(
        id = "questionChapterId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        nextQuestionId = "nextQuestionId",
        consultationId = "consultationId",
        urlImage = null,
        description = "description",
        transcriptionImage = null,
    )

    private val expectedQuestionChapterJson = QuestionChapterJson(
        id = "questionChapterId",
        title = "title",
        popupDescription = "popupDescription",
        order = 1,
        description = "description",
        nextQuestionId = "nextQuestionId",
        imageUrl = null,
        imageTranscription = null,
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
        questionProgressDescription = "Question 1 sur 1",
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
        questionProgressDescription = "Question 1 sur 1",
        possibleChoices = listOf(expectedChoixPossibleConditionalJson),
    )

    @Test
    fun `toJson - when QuestionUniqueChoice - should return QuestionUniqueChoiceJson only`() {
        // When
        val result = questionJsonMapper.toJson(
            Questions(
                questionCount = 10,
                questions = listOf(questionUniqueChoice)
            ),
        )

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionCount = 10,
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
        val questionsMultipleChoice = Questions(
            questionCount = 10,
            questions = listOf(questionChoixMultiple.copy(order = 1)),
        )

        // When
        val result = questionJsonMapper.toJson(questionsMultipleChoice)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionCount = 10,
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
        val result = questionJsonMapper.toJson(
            Questions(
                questionCount = 11,
                questions = listOf(questionOpen),
            ),
        )

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionCount = 11,
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
        val result = questionJsonMapper.toJson(
            Questions(
                questionCount = 12,
                questions = listOf(questionChapter),
            ),
        )

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionCount = 12,
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
        val result = questionJsonMapper.toJson(
            Questions(
                questionCount = 13,
                questions = listOf(questionConditional),
            ),
        )

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionCount = 13,
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
        // When
        val result = questionJsonMapper.toJson(
            Questions(
                questionCount = 10,
                questions = listOf(
                    questionOpen.copy(order = 1),
                    questionChapter.copy(order = 2),
                    questionChoixMultiple.copy(order = 3),
                    questionChapter.copy(order = 4),
                    questionUniqueChoice.copy(order = 5),
                )
            )
        )

        // Then
        assertThat(result.questionsOpened[0].questionProgress).isEqualTo("Question 1/3")
        assertThat(result.questionsMultipleChoices[0].questionProgress).isEqualTo("Question 2/3")
        assertThat(result.questionsUniqueChoice[0].questionProgress).isEqualTo("Question 3/3")
    }
}
