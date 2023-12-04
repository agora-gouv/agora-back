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
    private lateinit var controlQuestionMultipleChoicesUseCase: ControlQuestionMultipleChoicesUseCase

    @MockBean
    private lateinit var controlQuestionOpenUseCase: ControlQuestionOpenUseCase

    @MockBean
    private lateinit var communControlQuestionUseCase: CommunControlQuestionUseCase

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
        then(controlQuestionMultipleChoicesUseCase).shouldHaveNoInteractions()
        then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
        then(communControlQuestionUseCase).shouldHaveNoInteractions()
    }

    @Nested
    inner class QuestionOpenTestCases {

        @Test
        fun `isResponseConsultationValid - when isTextFieldOverMaxLength returns true - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val question = mock(QuestionOpen::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(controlQuestionOpenUseCase.isTextFieldOverMaxLength(response)).willReturn(true)

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
            then(controlQuestionOpenUseCase).should(only()).isTextFieldOverMaxLength(response)
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoInteractions()
            then(communControlQuestionUseCase).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isTextFieldOverMaxLength returns false - should return true`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
            val question = mock(QuestionOpen::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(controlQuestionOpenUseCase.isTextFieldOverMaxLength(response)).willReturn(false)

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
            then(controlQuestionOpenUseCase).should(only()).isTextFieldOverMaxLength(response)
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoInteractions()
            then(communControlQuestionUseCase).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class QuestionUniqueAndConditionalTestCases {

        @Test
        fun `isResponseConsultationValid - when isChoiceIdOverOne returns false and isChoiceAutreOverMaxLength returns true- should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(communControlQuestionUseCase.isChoiceIdOverOne(response.choiceIds!!)).willReturn(false)
            given(communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)).willReturn(true)

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
            then(communControlQuestionUseCase).should(times(1)).isChoiceIdOverOne(response.choiceIds!!)
            then(communControlQuestionUseCase).should(times(1)).isChoiceAutreOverMaxLength(response)
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isChoiceIdOverOne returns true and isChoiceAutreOverMaxLength returns false - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionConditional::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(communControlQuestionUseCase.isChoiceIdOverOne(response.choiceIds!!)).willReturn(true)
            given(communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)).willReturn(false)

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
            then(communControlQuestionUseCase).should(only()).isChoiceIdOverOne(response.choiceIds!!)
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isChoiceIdOverOne returns false and isChoiceAutreOverMaxLength returns false - should return true`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionConditional::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(communControlQuestionUseCase.isChoiceIdOverOne(response.choiceIds!!)).willReturn(false)
            given(communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)).willReturn(false)

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
            then(communControlQuestionUseCase).should(times(1)).isChoiceIdOverOne(response.choiceIds!!)
            then(communControlQuestionUseCase).should(times(1)).isChoiceAutreOverMaxLength(response)
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoInteractions()
        }

    }

    @Nested
    inner class QuestionMultipleChoicesTestCases {

        @Test
        fun `isResponseConsultationValid - when isChoiceIdDuplicated returns true - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(controlQuestionMultipleChoicesUseCase.isChoiceIdDuplicated(response.choiceIds!!)).willReturn(true)

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
            then(controlQuestionMultipleChoicesUseCase).should(only()).isChoiceIdDuplicated(response.choiceIds!!)
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(communControlQuestionUseCase).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isChoiceIdDuplicated returns false and isChoiceIdListOverMaxChoices returns true - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(controlQuestionMultipleChoicesUseCase.isChoiceIdDuplicated(response.choiceIds!!)).willReturn(false)
            given(
                controlQuestionMultipleChoicesUseCase.isChoiceIdListOverMaxChoices(
                    response.choiceIds!!,
                    question
                )
            ).willReturn(true)

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
            then(controlQuestionMultipleChoicesUseCase).should(times(1)).isChoiceIdDuplicated(response.choiceIds!!)
            then(controlQuestionMultipleChoicesUseCase).should(times(1))
                .isChoiceIdListOverMaxChoices(response.choiceIds!!, question)
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoMoreInteractions()
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(communControlQuestionUseCase).shouldHaveNoInteractions()
        }

        @Test
        fun `isResponseConsultationValid - when isChoiceIdDuplicated returns false and isChoiceIdListOverMaxChoices returns false and isChoiceAutreOverMaxLength returns true - should return false`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(controlQuestionMultipleChoicesUseCase.isChoiceIdDuplicated(response.choiceIds!!)).willReturn(false)
            given(
                controlQuestionMultipleChoicesUseCase.isChoiceIdListOverMaxChoices(
                    response.choiceIds!!,
                    question
                )
            ).willReturn(false)
            given(communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)).willReturn(true)

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
            then(controlQuestionMultipleChoicesUseCase).should(times(1)).isChoiceIdDuplicated(response.choiceIds!!)
            then(controlQuestionMultipleChoicesUseCase).should(times(1))
                .isChoiceIdListOverMaxChoices(response.choiceIds!!, question)
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoMoreInteractions()
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(communControlQuestionUseCase).should(only()).isChoiceAutreOverMaxLength(response)
        }

        @Test
        fun `isResponseConsultationValid - when isChoiceIdDuplicated returns false and isChoiceIdListOverMaxChoices returns false and isChoiceAutreOverMaxLength returns false - should return true`() {
            //Given
            val response =
                mock(ReponseConsultationInserting::class.java).also {
                    given(it.questionId).willReturn("question1")
                    given(it.choiceIds).willReturn(listOf("choiceId"))
                }
            val question = mock(QuestionMultipleChoices::class.java).also { given(it.id).willReturn("question1") }
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId")).willReturn(
                listOf(
                    question
                )
            )
            given(controlQuestionMultipleChoicesUseCase.isChoiceIdDuplicated(response.choiceIds!!)).willReturn(false)
            given(
                controlQuestionMultipleChoicesUseCase.isChoiceIdListOverMaxChoices(
                    response.choiceIds!!,
                    question
                )
            ).willReturn(false)
            given(communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)).willReturn(false)

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
            then(controlQuestionMultipleChoicesUseCase).should(times(1)).isChoiceIdDuplicated(response.choiceIds!!)
            then(controlQuestionMultipleChoicesUseCase).should(times(1))
                .isChoiceIdListOverMaxChoices(response.choiceIds!!, question)
            then(controlQuestionMultipleChoicesUseCase).shouldHaveNoMoreInteractions()
            then(controlQuestionOpenUseCase).shouldHaveNoInteractions()
            then(communControlQuestionUseCase).should(only()).isChoiceAutreOverMaxLength(response)
        }
    }
}