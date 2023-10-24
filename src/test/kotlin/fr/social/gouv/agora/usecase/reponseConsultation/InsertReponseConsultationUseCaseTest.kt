package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.TestUtils
import fr.social.gouv.agora.domain.QuestionOpen
import fr.social.gouv.agora.domain.QuestionUniqueChoice
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class InsertReponseConsultationUseCaseTest {

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

    @MockBean
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    companion object {
        private const val OTHER_QUESTION_MAX_LENGTH = 200
        private const val OPEN_QUESTION_MAX_LENGTH = 400
    }

    private val consultationInfo = ConsultationInfo(
        id = "consultId",
        title = "consultTitle",
        coverUrl = "",
        startDate = Date(0),
        endDate = LocalDateTime.of(2024, Month.OCTOBER, 19, 19, 0, 0).toDate(),
        questionCount = "",
        estimatedTime = "",
        participantCountGoal = 1,
        description = "",
        tipsDescription = "",
        thematiqueId = "",
    )

    @Test
    fun `insertReponseConsultation - when consultation is already finished - should return failure`() {
        // Given
        val todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0)
        val endDate = LocalDateTime.of(2023, Month.JUNE, 20, 14, 0, 0)
        val consultationFinished = consultationInfo.copy(endDate = endDate.toDate())
        mockDate(todayDate)
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationFinished)

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(consultationResponseRepository).shouldHaveNoInteractions()
        then(consultationPreviewAnsweredRepository).shouldHaveNoInteractions()
        then(insertReponseConsultationRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationPreviewPageRepository).shouldHaveNoInteractions()
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when has already answered and consultation not finished - should return failure`() {
        // Given
        val todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0)
        mockDate(todayDate)
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
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
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(consultationPreviewAnsweredRepository).shouldHaveNoInteractions()
        then(insertReponseConsultationRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationPreviewPageRepository).shouldHaveNoInteractions()
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and consultation not finished and answer open question - should delete ConsultationAnswered cache and sanitize with open_text_max_length then return result from insert repository`() {
        // Given
        val todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0)
        mockDate(todayDate)
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
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
        val consultationResponsesSanitized = listOf(
            ReponseConsultationInserting(
                questionId = "question1",
                choiceIds = null,
                responseText = "sanitizedSalut",
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
                consultationResponses = consultationResponsesSanitized,
            )
        ).willReturn(InsertResult.INSERT_SUCCESS)

        given(contentSanitizer.sanitize("salut", OPEN_QUESTION_MAX_LENGTH)).willReturn("sanitizedSalut")

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = consultationResponses,
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only())
            .deleteConsultationAnsweredListFromCache(userId = "userId")
        then(consultationPreviewPageRepository).should(times(1)).evictConsultationPreviewOngoingList(userId = "userId")
        then(consultationPreviewPageRepository).should(times(1)).evictConsultationPreviewAnsweredList(userId = "userId")
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = consultationResponsesSanitized,
        )
        then(questionRepository).should(times(1)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).should(only()).sanitize("salut", OPEN_QUESTION_MAX_LENGTH)
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and consultation not finished and answer unique question with choice other - should delete ConsultationAnswered cache and sanitize with other_text_max_length then return result from insert repository`() {
        // Given
        val todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0)
        mockDate(todayDate)
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
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
        val consultationResponsesSanitized = listOf(
            ReponseConsultationInserting(
                questionId = "question1",
                choiceIds = listOf("11111111-2222-0000-9999-5555555555555"),
                responseText = "sanitized autre choix",
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
                consultationResponses = consultationResponsesSanitized,
            )
        ).willReturn(InsertResult.INSERT_SUCCESS)

        given(contentSanitizer.sanitize("autre choix", OTHER_QUESTION_MAX_LENGTH)).willReturn("sanitized autre choix")
        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = consultationResponses,
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only())
            .deleteConsultationAnsweredListFromCache(userId = "userId")
        then(consultationPreviewPageRepository).should(times(1)).evictConsultationPreviewOngoingList(userId = "userId")
        then(consultationPreviewPageRepository).should(times(1)).evictConsultationPreviewAnsweredList(userId = "userId")
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = consultationResponsesSanitized,
        )
        then(questionRepository).should(times(1)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).should(only()).sanitize("autre choix", OTHER_QUESTION_MAX_LENGTH)
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and consultation not finished and has missing response on questions with condition - should delete ConsultationAnswered cache then return result from insert repository with added responses`() {
        // Given
        val todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0)
        mockDate(todayDate)
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
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
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only())
            .deleteConsultationAnsweredListFromCache(userId = "userId")
        then(consultationPreviewPageRepository).should(times(1)).evictConsultationPreviewOngoingList(userId = "userId")
        then(consultationPreviewPageRepository).should(times(1)).evictConsultationPreviewAnsweredList(userId = "userId")
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = listOf(addedResponseUniqueChoice),
        )
        then(questionRepository).should(times(1)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    private fun mockDate(localDateTime: LocalDateTime) {
        useCase = InsertReponseConsultationUseCase(
            contentSanitizer = contentSanitizer,
            consultationPreviewAnsweredRepository = consultationPreviewAnsweredRepository,
            insertReponseConsultationRepository = insertReponseConsultationRepository,
            consultationResponseRepository = consultationResponseRepository,
            questionRepository = questionRepository,
            insertConsultationResponseParametersMapper = insertConsultationResponseParametersMapper,
            consultationPreviewPageRepository = consultationPreviewPageRepository,
            consultationInfoRepository = consultationInfoRepository,
            clock = TestUtils.getFixedClock(localDateTime),
        )
    }
}