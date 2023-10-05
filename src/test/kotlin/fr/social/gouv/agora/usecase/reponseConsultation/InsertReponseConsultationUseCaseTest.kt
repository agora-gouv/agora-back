package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.QuestionOpen
import fr.social.gouv.agora.domain.QuestionUniqueChoice
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.social.gouv.agora.usecase.qag.ContentSanitizer
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertParameters
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
internal class InsertReponseConsultationUseCaseTest {

    @Autowired
    private lateinit var useCase: InsertReponseConsultationUseCase

    @MockBean
    private lateinit var consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository

    @MockBean
    private lateinit var insertReponseConsultationRepository: InsertReponseConsultationRepository

    @MockBean
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    @MockBean
    private lateinit var questionRepository: QuestionRepository

    @MockBean
    private lateinit var consultationPreviewPageRepository: ConsultationPreviewPageRepository

    @MockBean
    private lateinit var insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper

    @MockBean
    private lateinit var contentSanitizer: ContentSanitizer

    companion object {
        private const val OTHER_QUESTION_MAX_LENGTH = 200
        private const val OPEN_QUESTION_MAX_LENGTH = 400
    }
    @Test
    fun `insertReponseConsultation - when has already answered - should return failure`() {
        // Given
        given(consultationResponseRepository.hasAnsweredConsultation(consultationId = "consultId", userId = "userId"))
            .willReturn(true)

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).shouldHaveNoInteractions()
        then(insertReponseConsultationRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationPreviewPageRepository).shouldHaveNoInteractions()
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and answer open question - should delete ConsultationAnswered cache and sanitize with open_text_max_length then return result from insert repository`() {
        // Given
        given(consultationResponseRepository.hasAnsweredConsultation(consultationId = "consultId", userId = "userId"))
            .willReturn(false)

        val questionList = listOf(
            mock(QuestionOpen::class.java).also { given(it.id).willReturn("question1") }
        )
        given(questionRepository.getConsultationQuestionList(consultationId = "consultId")).willReturn(questionList)

        val consultationResponses = listOf(
            ReponseConsultationInserting(
                questionId = "question1",
                choiceIds = null,
                responseText = "salut",
            )
        )
        val insertParameters = mock(InsertParameters::class.java)

        given(
            insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = "consultId",
                userId = "userId",
            )
        ).willReturn(insertParameters)

        given(
            insertReponseConsultationRepository.insertConsultationResponses(
                insertParameters = insertParameters,
                consultationResponses = consultationResponses,
            )
        ).willReturn(InsertResult.INSERT_SUCCESS)

        given(contentSanitizer.sanitize("salut", OPEN_QUESTION_MAX_LENGTH)).willReturn("salut")
        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = consultationResponses,
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only())
            .deleteConsultationAnsweredListFromCache(userId = "userId")
        inOrder(consultationPreviewPageRepository).also {
            then(consultationPreviewPageRepository).should(it)
                .evictConsultationPreviewOngoingList(userId = "userId")
            then(consultationPreviewPageRepository).should(it)
                .evictConsultationPreviewAnsweredList(userId = "userId")
            it.verifyNoMoreInteractions()
        }
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = consultationResponses,
        )
        then(questionRepository).should(times(2)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).should(only()).sanitize("salut", OPEN_QUESTION_MAX_LENGTH)
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and answer unique question with choice other - should delete ConsultationAnswered cache and sanitize with other_text_max_length then return result from insert repository`() {
        // Given
        given(consultationResponseRepository.hasAnsweredConsultation(consultationId = "consultId", userId = "userId"))
            .willReturn(false)

        val questionList = listOf(
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") }
        )
        given(questionRepository.getConsultationQuestionList(consultationId = "consultId")).willReturn(questionList)

        val consultationResponses = listOf(
            ReponseConsultationInserting(
                questionId = "question1",
                choiceIds = listOf("11111111-2222-0000-9999-5555555555555"),
                responseText = "autre choix",
            )
        )
        val insertParameters = mock(InsertParameters::class.java)

        given(
            insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = "consultId",
                userId = "userId",
            )
        ).willReturn(insertParameters)

        given(
            insertReponseConsultationRepository.insertConsultationResponses(
                insertParameters = insertParameters,
                consultationResponses = consultationResponses,
            )
        ).willReturn(InsertResult.INSERT_SUCCESS)

        given(contentSanitizer.sanitize("autre choix", OTHER_QUESTION_MAX_LENGTH)).willReturn("autre choix")
        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = consultationResponses,
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only())
            .deleteConsultationAnsweredListFromCache(userId = "userId")
        inOrder(consultationPreviewPageRepository).also {
            then(consultationPreviewPageRepository).should(it)
                .evictConsultationPreviewOngoingList(userId = "userId")
            then(consultationPreviewPageRepository).should(it)
                .evictConsultationPreviewAnsweredList(userId = "userId")
            it.verifyNoMoreInteractions()
        }
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = consultationResponses,
        )
        then(questionRepository).should(times(2)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).should(only()).sanitize("autre choix", OTHER_QUESTION_MAX_LENGTH)
    }

    @Test
    fun `insertReponseConsultation - when has missing response on questions with condition - should delete ConsultationAnswered cache then return result from insert repository with added responses`() {
        // Given
        given(
            consultationResponseRepository.hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId"
            )
        )
            .willReturn(false)

        val questionList = listOf(
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") },
            mock(QuestionOpen::class.java).also { given(it.id).willReturn("question2") }
        )
        given(questionRepository.getConsultationQuestionList(consultationId = "consultId")).willReturn(
            questionList
        )

        val insertParameters = mock(InsertParameters::class.java)
        given(
            insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = "consultId",
                userId = "userId",
            )
        ).willReturn(insertParameters)

        val addedResponseUniqueChoice = ReponseConsultationInserting(
            questionId = "question1",
            choiceIds = listOf("11111111-1111-1111-1111-111111111111"),
            responseText = "",
        )
        given(
            insertReponseConsultationRepository.insertConsultationResponses(
                insertParameters = insertParameters,
                consultationResponses = listOf(addedResponseUniqueChoice),
            )
        ).willReturn(InsertResult.INSERT_SUCCESS)

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = emptyList(),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only())
            .deleteConsultationAnsweredListFromCache(userId = "userId")
        inOrder(consultationPreviewPageRepository).also {
            then(consultationPreviewPageRepository).should(it)
                .evictConsultationPreviewOngoingList(userId = "userId")
            then(consultationPreviewPageRepository).should(it)
                .evictConsultationPreviewAnsweredList(userId = "userId")
            it.verifyNoMoreInteractions()
        }
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = listOf(addedResponseUniqueChoice),
        )
        then(questionRepository).should(times(2)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).shouldHaveNoInteractions()
    }

}