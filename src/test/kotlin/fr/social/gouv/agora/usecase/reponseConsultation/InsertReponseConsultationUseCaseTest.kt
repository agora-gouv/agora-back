package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.QuestionOpen
import fr.social.gouv.agora.domain.QuestionUniqueChoice
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
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
    private lateinit var insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper

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
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and answer all questions - should delete ConsultationAnswered cache then return result from insert repository`() {
        // Given
        given(consultationResponseRepository.hasAnsweredConsultation(consultationId = "consultId", userId = "userId"))
            .willReturn(false)

        val questionList = listOf(
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") }
        )
        given(questionRepository.getConsultationQuestionList(consultationId = "consultId")).willReturn(questionList)

        val consultationResponses = listOf(
            mock(ReponseConsultationInserting::class.java).also { given(it.questionId).willReturn("question1") }
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
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = consultationResponses,
        )
    }

    @Test
    fun `insertReponseConsultation - when has missing response on questions with condition - should delete ConsultationAnswered cache then return result from insert repository with added responses`() {
        // Given
        given(consultationResponseRepository.hasAnsweredConsultation(consultationId = "consultId", userId = "userId"))
            .willReturn(false)

        val questionList = listOf(
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") },
            mock(QuestionOpen::class.java).also { given(it.id).willReturn("question2") }
        )
        given(questionRepository.getConsultationQuestionList(consultationId = "consultId")).willReturn(questionList)

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
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = listOf(addedResponseUniqueChoice),
        )
    }

}