package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ControlResponseConsultationUseCaseTest {

    @Autowired
    private lateinit var useCase: ControlResponseConsultationUseCase

    @MockBean
    private lateinit var questionRepository: QuestionRepository

    @MockBean
    private lateinit var questionMultipleChoicesValidator: QuestionMultipleChoicesValidator

    @MockBean
    private lateinit var questionUniqueChoiceAndConditionalValidator: QuestionUniqueChoiceAndConditionalValidator

    @MockBean
    private lateinit var questionOpenValidator: QuestionOpenValidator

    @Test
    fun `isResponseConsultationValid - when response has duplicated questionId - should return false`() {
        // Given
        val questionList = listOf(
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") },
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question2") }
        )
        given(questionRepository.getConsultationQuestionList(consultationId = "consultId"))
            .willReturn(questionList)

        // When
        val result = useCase.isResponseConsultationValid(
            consultationId = "consultId",
            consultationResponses = listOf(
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") },
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") })
        )

        // Then
        assertThat(result).isEqualTo(false)
        then(questionRepository).should(only()).getConsultationQuestionList(
            consultationId = "consultId",
        )
        then(questionOpenValidator).shouldHaveNoInteractions()
        then(questionMultipleChoicesValidator).shouldHaveNoInteractions()
        then(questionUniqueChoiceAndConditionalValidator).shouldHaveNoInteractions()
    }

    @Nested
    inner class QuestionOpenTestCases {

        @Test
        fun `isResponseConsultationValid - when isValid returns true - should return true`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val question = mock(QuestionOpen::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(questionOpenValidator.isValid(question, response)).willReturn(true)

            //When
            val result = useCase.isResponseConsultationValid(
                consultationId = "consultationId",
                consultationResponses = listOf(response)
            )

            //Then
            assertThat(result).isEqualTo(true)
            then(questionRepository).should(only()).getConsultationQuestionList(
                consultationId = "consultationId",
            )
            then(questionOpenValidator).should(only()).isValid(question, response)
            then(questionMultipleChoicesValidator).shouldHaveNoInteractions()
            then(questionUniqueChoiceAndConditionalValidator).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isValid returns false - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val question = mock(QuestionOpen::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(questionOpenValidator.isValid(question, response)).willReturn(false)

            //When
            val result = useCase.isResponseConsultationValid(
                consultationId = "consultationId",
                consultationResponses = listOf(response)
            )

            //Then
            assertThat(result).isEqualTo(false)
            then(questionRepository).should(only()).getConsultationQuestionList(
                consultationId = "consultationId",
            )
            then(questionOpenValidator).should(only()).isValid(question, response)
            then(questionMultipleChoicesValidator).shouldHaveNoInteractions()
            then(questionUniqueChoiceAndConditionalValidator).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class QuestionMultipleTestCases {

        @Test
        fun `isResponseConsultationValid - when isValid returns true - should return true`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(questionMultipleChoicesValidator.isValid(question, response)).willReturn(true)

            //When
            val result = useCase.isResponseConsultationValid(
                consultationId = "consultationId",
                consultationResponses = listOf(response)
            )

            //Then
            assertThat(result).isEqualTo(true)
            then(questionRepository).should(only()).getConsultationQuestionList(
                consultationId = "consultationId",
            )
            then(questionOpenValidator).shouldHaveNoInteractions()
            then(questionMultipleChoicesValidator).should(only()).isValid(question, response)
            then(questionUniqueChoiceAndConditionalValidator).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isValid returns false - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(questionMultipleChoicesValidator.isValid(question, response)).willReturn(false)

            //When
            val result = useCase.isResponseConsultationValid(
                consultationId = "consultationId",
                consultationResponses = listOf(response)
            )

            //Then
            assertThat(result).isEqualTo(false)
            then(questionRepository).should(only()).getConsultationQuestionList(
                consultationId = "consultationId",
            )
            then(questionOpenValidator).shouldHaveNoInteractions()
            then(questionMultipleChoicesValidator).should(only()).isValid(question, response)
            then(questionUniqueChoiceAndConditionalValidator).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class QuestionUniqueChoiceAndConditionalTestCases {

        @Test
        fun `isResponseConsultationValid - when isValid returns true for QuestionUniqueChoice and Conditional  - should return true`() {
            //Given
            val response1 =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val response2 =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question2") }
            val question1 = mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") }
            val question2 = mock(QuestionConditional::class.java).also { given(it.id).willReturn("question2") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question1, question2
                )
            )
            given(questionUniqueChoiceAndConditionalValidator.isValid(question1, response1)).willReturn(true)
            given(questionUniqueChoiceAndConditionalValidator.isValid(question2, response2)).willReturn(true)

            //When
            val result = useCase.isResponseConsultationValid(
                consultationId = "consultationId",
                consultationResponses = listOf(response1, response2)
            )

            //Then
            assertThat(result).isEqualTo(true)
            then(questionRepository).should(only()).getConsultationQuestionList(
                consultationId = "consultationId",
            )
            then(questionOpenValidator).shouldHaveNoInteractions()
            then(questionMultipleChoicesValidator).shouldHaveNoInteractions()
            then(questionUniqueChoiceAndConditionalValidator).should().isValid(question1, response1)
            then(questionUniqueChoiceAndConditionalValidator).should().isValid(question2, response2)

        }

        @Test
        fun `isResponseConsultationValid - when isValid returns true for QuestionUniqueChoice and false for Conditional  - should return false`() {
            //Given
            val response1 =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val response2 =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question2") }
            val question1 = mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") }
            val question2 = mock(QuestionConditional::class.java).also { given(it.id).willReturn("question2") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question1, question2
                )
            )
            given(questionUniqueChoiceAndConditionalValidator.isValid(question1, response1)).willReturn(true)
            given(questionUniqueChoiceAndConditionalValidator.isValid(question2, response2)).willReturn(false)

            //When
            val result = useCase.isResponseConsultationValid(
                consultationId = "consultationId",
                consultationResponses = listOf(response1, response2)
            )

            //Then
            assertThat(result).isEqualTo(false)
            then(questionRepository).should(only()).getConsultationQuestionList(
                consultationId = "consultationId",
            )
            then(questionOpenValidator).shouldHaveNoInteractions()
            then(questionMultipleChoicesValidator).shouldHaveNoInteractions()
            then(questionUniqueChoiceAndConditionalValidator).should().isValid(question1, response1)
            then(questionUniqueChoiceAndConditionalValidator).should().isValid(question2, response2)
        }
    }
}