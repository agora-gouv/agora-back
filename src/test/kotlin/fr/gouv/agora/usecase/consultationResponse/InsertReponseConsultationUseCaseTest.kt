package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.domain.ReponseConsultationInserting
import fr.gouv.agora.domain.UserAnsweredConsultation
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationAnsweredPaginatedListCacheRepository
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertParameters
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheRepository
import fr.gouv.agora.usecase.qag.ContentSanitizer
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class InsertReponseConsultationUseCaseTest {

    private lateinit var useCase: InsertReponseConsultationUseCase

    @Mock
    private lateinit var insertReponseConsultationRepository: InsertReponseConsultationRepository

    @Mock
    private lateinit var userAnsweredConsultationRepository: UserAnsweredConsultationRepository

    @Mock
    private lateinit var questionRepository: QuestionRepository

    @Mock
    private lateinit var consultationPreviewPageRepository: ConsultationPreviewPageRepository

    @Mock
    private lateinit var insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper

    @Mock
    private lateinit var contentSanitizer: ContentSanitizer

    @Mock
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @Mock
    private lateinit var consultationDetailsV2CacheRepository: ConsultationDetailsV2CacheRepository

    @Mock
    private lateinit var consultationAnsweredPaginatedListCacheRepository: ConsultationAnsweredPaginatedListCacheRepository

    @Mock
    private lateinit var consultationResultsCacheRepository: ConsultationResultsCacheRepository

    companion object {
        private const val OTHER_QUESTION_MAX_LENGTH = 200
        private const val OPEN_QUESTION_MAX_LENGTH = 400
    }

    private val consultationInfo = ConsultationInfo(
        id = "consultId",
        title = "consultTitle",
        coverUrl = "",
        detailsCoverUrl = "",
        startDate = LocalDateTime.MIN,
        endDate = LocalDateTime.of(2024, Month.OCTOBER, 19, 19, 0, 0),
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
        val endDate = LocalDateTime.of(2023, Month.JUNE, 20, 14, 0, 0)
        val consultationFinished = consultationInfo.copy(endDate = endDate)
        mockDate(todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0))
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
        then(userAnsweredConsultationRepository).shouldHaveNoInteractions()
        then(insertReponseConsultationRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationPreviewPageRepository).shouldHaveNoInteractions()
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when has already answered and consultation not finished - should return failure`() {
        // Given
        mockDate(todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0))
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
        given(
            userAnsweredConsultationRepository.hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId"
            )
        )
            .willReturn(true)

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultId",
            userId = "userId",
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(userAnsweredConsultationRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(insertReponseConsultationRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationPreviewPageRepository).shouldHaveNoInteractions()
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet and consultation not finished and answer open question - should delete ConsultationAnswered cache and sanitize with open_text_max_length then return result from insert repository`() {
        // Given
        mockDate(todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0))
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
        given(
            userAnsweredConsultationRepository.hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId"
            )
        )
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
        then(userAnsweredConsultationRepository).should().hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(userAnsweredConsultationRepository).should().insertUserAnsweredConsultation(
            UserAnsweredConsultation(userId = "userId", consultationId = "consultId")
        )
        then(userAnsweredConsultationRepository).shouldHaveNoMoreInteractions()
        then(consultationDetailsV2CacheRepository).should(only())
            .evictHasAnsweredConsultation(consultationId = "consultId", userId = "userId")
        then(consultationPreviewPageRepository).should(only()).evictConsultationPreviewAnsweredList(userId = "userId")
        then(consultationAnsweredPaginatedListCacheRepository).should(only()).clearCache(userId = "userId")
        then(consultationResultsCacheRepository).should(only())
            .evictConsultationResultsCache(consultationId = "consultId")
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
        mockDate(todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0))
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
        given(
            userAnsweredConsultationRepository.hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId"
            )
        )
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
        then(userAnsweredConsultationRepository).should().hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(userAnsweredConsultationRepository).should().insertUserAnsweredConsultation(
            UserAnsweredConsultation(userId = "userId", consultationId = "consultId")
        )
        then(userAnsweredConsultationRepository).shouldHaveNoMoreInteractions()
        then(consultationDetailsV2CacheRepository).should(only())
            .evictHasAnsweredConsultation(consultationId = "consultId", userId = "userId")
        then(consultationPreviewPageRepository).should(only()).evictConsultationPreviewAnsweredList(userId = "userId")
        then(consultationAnsweredPaginatedListCacheRepository).should(only()).clearCache(userId = "userId")
        then(consultationResultsCacheRepository).should(only())
            .evictConsultationResultsCache(consultationId = "consultId")
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
        mockDate(todayDate = LocalDateTime.of(2023, Month.OCTOBER, 19, 19, 0, 0))
        given(consultationInfoRepository.getConsultation(consultationId = "consultId")).willReturn(consultationInfo)
        given(
            userAnsweredConsultationRepository.hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId",
            )
        )
            .willReturn(false)

        val questionList = listOf(
            mock(QuestionUniqueChoice::class.java).also { given(it.id).willReturn("question1") },
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
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultId")
        then(userAnsweredConsultationRepository).should().hasAnsweredConsultation(
            consultationId = "consultId",
            userId = "userId",
        )
        then(userAnsweredConsultationRepository).should().insertUserAnsweredConsultation(
            UserAnsweredConsultation(userId = "userId", consultationId = "consultId")
        )
        then(userAnsweredConsultationRepository).shouldHaveNoMoreInteractions()
        then(consultationDetailsV2CacheRepository).should(only())
            .evictHasAnsweredConsultation(consultationId = "consultId", userId = "userId")
        then(consultationPreviewPageRepository).should(only()).evictConsultationPreviewAnsweredList(userId = "userId")
        then(consultationAnsweredPaginatedListCacheRepository).should(only()).clearCache(userId = "userId")
        then(consultationResultsCacheRepository).should(only())
            .evictConsultationResultsCache(consultationId = "consultId")
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = listOf(addedResponseUniqueChoice),
        )
        then(questionRepository).should(times(1)).getConsultationQuestionList(consultationId = "consultId")
        then(contentSanitizer).shouldHaveNoInteractions()
    }

    private fun mockDate(todayDate: LocalDateTime) {
        useCase = InsertReponseConsultationUseCase(
            contentSanitizer = contentSanitizer,
            insertReponseConsultationRepository = insertReponseConsultationRepository,
            userAnsweredConsultationRepository = userAnsweredConsultationRepository,
            questionRepository = questionRepository,
            insertConsultationResponseParametersMapper = insertConsultationResponseParametersMapper,
            consultationPreviewPageRepository = consultationPreviewPageRepository,
            consultationInfoRepository = consultationInfoRepository,
            consultationDetailsV2CacheRepository = consultationDetailsV2CacheRepository,
            consultationAnsweredPaginatedListCacheRepository = consultationAnsweredPaginatedListCacheRepository,
            consultationResultsCacheRepository = consultationResultsCacheRepository,
            clock = TestUtils.getFixedClock(todayDate),
        )
    }
}
