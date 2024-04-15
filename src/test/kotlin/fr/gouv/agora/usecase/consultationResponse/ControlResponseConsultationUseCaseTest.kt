package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ControlResponseConsultationUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ControlResponseConsultationUseCase

    @Mock
    private lateinit var questionRepository: QuestionRepository

    @Mock
    private lateinit var questionMultipleChoicesValidator: QuestionMultipleChoicesValidator

    @Mock
    private lateinit var questionUniqueChoiceAndConditionalValidator: QuestionUniqueChoiceAndConditionalValidator

    @Mock
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

        private lateinit var question: QuestionOpen
        private lateinit var response: ReponseConsultationInserting

        @BeforeEach

        fun setUp() {
            question = mock(QuestionOpen::class.java).also { given(it.id).willReturn("question1") }
            response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
        }

        @Test
        fun `isResponseConsultationValid - when isValid returns true - should return true`() {
            //Given
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

        private lateinit var question: QuestionMultipleChoices
        private lateinit var response: ReponseConsultationInserting

        @BeforeEach
        fun setUp() {
            question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
        }

        @Test
        fun `isResponseConsultationValid - when isValid returns true - should return true`() {
            //Given
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
        private lateinit var question1: QuestionUniqueChoice
        private lateinit var question2: QuestionConditional
        private lateinit var response1: ReponseConsultationInserting
        private lateinit var response2: ReponseConsultationInserting

        @BeforeEach
        fun setUp() {
            question1 = mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") }
            question2 = mock(QuestionConditional::class.java).also { given(it.id).willReturn("question2") }
            response1 =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            response2 =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question2") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question1, question2
                )
            )
        }

        @Test
        fun `isResponseConsultationValid - when isValid returns true for QuestionUniqueChoice and Conditional  - should return true`() {
            //Given
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