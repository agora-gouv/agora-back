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

    private val choixPossible = ChoixPossible(
        id = "choix_id",
        label = "dto-label",
        ordre = 1,
        questionId = "question_id",
    )

    private val questionChoixUnique = QuestionChoixUnique(
        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
        title = "dto-label",
        order = 1,
        consultationId = "c29255f2-10ca-4be5-aab1-801ea173337c",
        choixPossibleList = emptyList(),
    )

    private val questionChoixUniqueWithChoixPossible = QuestionChoixUnique(
        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
        title = "dto-label",
        order = 1,
        consultationId = "c29255f2-10ca-4be5-aab1-801ea173337c",
        choixPossibleList = listOf(choixPossible),
    )

    private val questionChoixMultiple = QuestionChoixMultiple(
        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
        title = "dto-label",
        order = 1,
        consultationId = "c29255f2-10ca-4be5-aab1-801ea173337c",
        choixPossibleList = emptyList(),
        maxChoices = 2
    )

    private val questionChoixMultipleWithChoixPossible = QuestionChoixMultiple(
        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
        title = "dto-label",
        order = 1,
        consultationId = "c29255f2-10ca-4be5-aab1-801ea173337c",
        choixPossibleList = listOf(choixPossible),
        maxChoices = 2
    )

    private val questionChapter = Chapitre(
        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
        title = "dto-label",
        order = 1,
        consultationId = "c29255f2-10ca-4be5-aab1-801ea173337c",
        description = "dto-description",
    )

    private val questionOpen = QuestionOuverte(
        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
        title = "dto-label",
        order = 1,
        consultationId = "c29255f2-10ca-4be5-aab1-801ea173337c",
    )

    @Test
    fun `toJson of list of QuestionChoixUnique with no ChoixPossible should return QuestionsJson with empty list for questionsOpened, questionsMultipleChoices, chapters `() {
        // Given
        val questionsUniqueChoice = listOf(questionChoixUnique.copy(order = 1))

        // When
        val result = questionJsonMapper.toJson(questionsUniqueChoice)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                chapters = emptyList(),
                questionsMultipleChoices = emptyList(),
                questionsOpened = emptyList(),
                questionsUniqueChoice = listOf(
                    QuestionUniqueChoiceJson(
                        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
                        title = "dto-label",
                        order = 1,
                        questionProgress = "Question 1/1",
                        possibleChoices = emptyList(),
                    )
                ),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson of list of QuestionChoixUnique with ChoixPossible should return QuestionsJson with empty list for questionsOpened, questionsMultipleChoices, chapters `() {
        // Given
        val questionsUniqueChoice = listOf(questionChoixUniqueWithChoixPossible.copy(order = 1))

        // When
        val result = questionJsonMapper.toJson(questionsUniqueChoice)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                chapters = emptyList(),
                questionsMultipleChoices = emptyList(),
                questionsOpened = emptyList(),
                questionsUniqueChoice = listOf(
                    QuestionUniqueChoiceJson(
                        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
                        title = "dto-label",
                        order = 1,
                        questionProgress = "Question 1/1",
                        possibleChoices = listOf(
                            ChoixPossibleJson(
                                id = "choix_id",
                                label = "dto-label",
                                order = 1,
                            )
                        )
                    )
                ),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson of list of QuestionChoixMultiple with no ChoixPossible should return QuestionsJson with empty list for questionsOpened, questionsUniqueChoice, chapters `() {
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
                questionsMultipleChoices = listOf(
                    QuestionMultipleChoicesJson(
                        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
                        title = "dto-label",
                        order = 1,
                        questionProgress = "Question 1/1",
                        maxChoices = 2,
                        possibleChoices = emptyList()
                    )
                ),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson of list of QuestionChoixMultiple with ChoixPossible should return QuestionsJson with empty list for questionsOpened, questionsUniqueChoice, chapters `() {
        // Given
        val questionsMultipleChoice = listOf(questionChoixMultipleWithChoixPossible.copy(order = 1))

        // When
        val result = questionJsonMapper.toJson(questionsMultipleChoice)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                chapters = emptyList(),
                questionsUniqueChoice = emptyList(),
                questionsOpened = emptyList(),
                questionsMultipleChoices = listOf(
                    QuestionMultipleChoicesJson(
                        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
                        title = "dto-label",
                        order = 1,
                        questionProgress = "Question 1/1",
                        maxChoices = 2,
                        possibleChoices = listOf(
                            ChoixPossibleJson(
                                id = "choix_id",
                                label = "dto-label",
                                order = 1,
                            )
                        )
                    )
                ),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson of list of Chapter should return QuestionsJson with empty list for questionsOpened, questionsUniqueChoice, questionsMultipleChoices `() {
        // Given
        val questionsChapter = listOf(questionChapter.copy(order = 1))

        // When
        val result = questionJsonMapper.toJson(questionsChapter)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionsMultipleChoices = emptyList(),
                questionsUniqueChoice = emptyList(),
                questionsOpened = emptyList(),
                chapters = listOf(
                    ChapterJson(
                        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
                        title = "dto-label",
                        order = 1,
                        description = "dto-description",
                    )
                ),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson of list of QuestionOpened should return QuestionsJson with empty list for questionsMultipleChoices, questionsUniqueChoice, chapters `() {
        // Given
        val questionsOpen = listOf(questionOpen.copy(order = 1))

        // When
        val result = questionJsonMapper.toJson(questionsOpen)

        // Then
        assertThat(result).isEqualTo(
            QuestionsJson(
                questionsMultipleChoices = emptyList(),
                questionsUniqueChoice = emptyList(),
                chapters = emptyList(),
                questionsOpened = listOf(
                    QuestionOpenedJson(
                        id = "c29255f2-10ca-4be5-aab1-801ea173337c",
                        title = "dto-label",
                        order = 1,
                        questionProgress = "Question 1/1",
                    )
                ),
                questionsWithCondition = emptyList(),
            )
        )
    }

    @Test
    fun `toJson of list of Questions should return the correct question Progress `() {
        // Given
        val questions = listOf(
            questionOpen.copy(order = 1),
            questionChapter.copy(order = 2),
            questionChoixMultiple.copy(order = 3),
            questionChapter.copy(order = 4),
            questionChoixMultiple.copy(order = 5),
        )

        // When
        val result = questionJsonMapper.toJson(questions)

        // Then
        assertThat(result.questionsOpened[0].questionProgress).isEqualTo("Question 1/3")
        assertThat(result.questionsMultipleChoices[0].questionProgress).isEqualTo("Question 2/3")
        assertThat(result.questionsMultipleChoices[1].questionProgress).isEqualTo("Question 3/3")
    }
}