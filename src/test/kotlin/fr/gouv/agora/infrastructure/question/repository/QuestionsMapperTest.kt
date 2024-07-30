package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class QuestionsMapperTest {

    @InjectMocks
    private lateinit var questionsMapper: QuestionsMapper

    @Mock
    private lateinit var choixPossibleMapper: ChoixPossibleMapper

    @Mock
    private lateinit var questionMapper: QuestionMapper

    private val questionUUID = UUID.randomUUID()
    private val consultationUUID = UUID.randomUUID()

    private val questionDTO = QuestionDTO(
        id = questionUUID,
        title = "title",
        popupDescription = "popupDescription",
        ordre = 1,
        type = "unknown",
        description = null,
        maxChoices = null,
        nextQuestionId = null,
        consultationId = consultationUUID,
    )

    @Nested
    inner class UniqueChoiceTestCases {

        private val questionUniqueChoiceDTO = questionDTO.copy(type = "unique")

        private val expectedQuestion = QuestionUniqueChoice(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            choixPossibleList = emptyList(),
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionUniqueChoice with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionUniqueChoiceDTO = questionUniqueChoiceDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionUniqueChoice with null nextQuestionId`() {
            // Given
            val questionUniqueChoiceDTO = questionUniqueChoiceDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionUniqueChoice with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionUniqueChoiceDTO = questionUniqueChoiceDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionUniqueChoiceDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = listOf(questionUniqueChoiceDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList - should return QuestionUniqueChoice with mapped choixPossible`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            val choixPossible = mock(ChoixPossibleDefault::class.java)
            given(choixPossibleMapper.toDefault(choixPossibleDTO)).willReturn(choixPossible)

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = listOf(choixPossible)))
        }

    }

    @Nested
    inner class MultipleChoicesTestCases {

        private val questionMultipleChoicesDTO = questionDTO.copy(type = "multiple", maxChoices = 5)

        private val expectedQuestion = QuestionMultipleChoices(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            choixPossibleList = emptyList(),
            maxChoices = 5,
        )

        @Test
        fun `toDomain - when has null maxChoices - should throw exception`() {
            // Given
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(maxChoices = null)

            // When
            val hasException = try {
                questionsMapper.toDomain(
                    dto = questionMultipleChoicesDTO,
                    questionDTOList = emptyList(),
                    choixPossibleDTOList = emptyList(),
                )
                false
            } catch (e: Exception) {
                true
            }

            // Then
            assertThat(hasException).isEqualTo(true)
        }

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionMultipleChoices with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionMultipleChoices with null nextQuestionId`() {
            // Given
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionMultipleChoices with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionMultipleChoicesDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = listOf(questionMultipleChoicesDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList - should return QuestionMultipleChoices with mapped choixPossible`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            val choixPossible = mock(ChoixPossibleDefault::class.java)
            given(choixPossibleMapper.toDefault(choixPossibleDTO)).willReturn(choixPossible)

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = listOf(choixPossible)))
        }

    }

    @Nested
    inner class OpenTestCases {

        private val questionOpenDTO = questionDTO.copy(type = "open")

        private val expectedQuestion = QuestionOpen(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionOpen with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionOpenDTO = questionOpenDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionOpenDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionOpen with null nextQuestionId`() {
            // Given
            val questionOpenDTO = questionOpenDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionOpenDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionOpen with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionOpenDTO = questionOpenDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionOpenDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionOpenDTO,
                questionDTOList = listOf(questionOpenDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

    }

    @Nested
    inner class ChapterTestCases {

        private val questionChapterDTO = questionDTO.copy(type = "chapter", description = "description")

        private val expectedQuestion = QuestionChapter(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            description = "description",
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionChapter with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionChapterDTO = questionChapterDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionChapterDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionChapter with null nextQuestionId`() {
            // Given
            val questionChapterDTO = questionChapterDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionChapterDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionChapter with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionChapterDTO = questionChapterDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionChapterDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionChapterDTO,
                questionDTOList = listOf(questionChapterDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

    }

    @Nested
    inner class ConditionalChoiceTestCases {

        private val questionConditionalDTO = questionDTO.copy(type = "conditional")

        private val expectedQuestion = QuestionConditional(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            choixPossibleList = emptyList(),
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionConditional with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionConditionalDTO = questionConditionalDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionConditional with null nextQuestionId`() {
            // Given
            val questionConditionalDTO = questionConditionalDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionConditional with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionConditionalDTO = questionConditionalDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionConditionalDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = listOf(questionConditionalDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList but mapped to null - should return QuestionConditional empty choixPossibleList`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            given(choixPossibleMapper.toConditional(choixPossibleDTO)).willReturn(null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = emptyList()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList mapped to an object - should return QuestionConditional with mapped choixPossible`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            val choixPossible = mock(ChoixPossibleConditional::class.java)
            given(choixPossibleMapper.toConditional(choixPossibleDTO)).willReturn(choixPossible)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = listOf(choixPossible)))
        }

    }

}
