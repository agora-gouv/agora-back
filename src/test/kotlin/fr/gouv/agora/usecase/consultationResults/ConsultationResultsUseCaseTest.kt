package fr.gouv.agora.usecase.consultationResults

import fr.gouv.agora.TestUtils.lenientGiven
import fr.gouv.agora.TestUtils.willReturn
import fr.gouv.agora.domain.ChoiceResults
import fr.gouv.agora.domain.ChoixPossibleDefault
import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionResults
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.domain.QuestionWithChoices
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheResult
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.only
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ConsultationResultsUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ConsultationResultsUseCase

    @Mock
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @Mock
    private lateinit var questionRepository: QuestionRepository

    @Mock
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    @Mock
    private lateinit var consultationResultAggregatedRepository: ConsultationResultAggregatedRepository

    @Mock
    private lateinit var userAnsweredConsultationRepository: UserAnsweredConsultationRepository

    @Mock
    private lateinit var mapper: QuestionNoResponseMapper

    @Mock
    private lateinit var cacheRepository: ConsultationResultsCacheRepository

    companion object {
        @JvmStatic
        fun getConsultationResultsParameters() = arrayOf(
            input(
                testDescription = "when 1 question with 1 choice & 1 response",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 1, expectedRatio = 1.0),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                ),
                participantCount = 1,
            ),
            input(
                testDescription = "when 1 question with 2 choice & some responses",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 2, expectedRatio = 0.6666666666666666),
                            ChoiceInputData(choiceId = "q2c2", responseCount = 1, expectedRatio = 0.3333333333333333),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                ),
                participantCount = 3,
            ),
            input(
                testDescription = "when 2 questions with choices & some responses",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 8, expectedRatio = 0.8),
                            ChoiceInputData(choiceId = "q1c2", responseCount = 2, expectedRatio = 0.2),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                    QuestionInputData(
                        questionId = "question2",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q2c1", responseCount = 4, expectedRatio = 0.4),
                            ChoiceInputData(choiceId = "q2c2", responseCount = 6, expectedRatio = 0.6),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                ),
                participantCount = 10,
            ),
            input(
                testDescription = "when questions with choices & some responses but some questions do not have choices",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 8, expectedRatio = 0.8),
                            ChoiceInputData(choiceId = "q1c2", responseCount = 2, expectedRatio = 0.2),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                    QuestionInputData(
                        questionId = "question2",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 4, expectedRatio = 0.4),
                            ChoiceInputData(choiceId = "q2c2", responseCount = 6, expectedRatio = 0.6),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                    QuestionInputData(
                        questionId = "question3",
                        choices = emptyList(),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                ),
                participantCount = 10,
            ),
            input(
                testDescription = "when has question with multiple choices",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 10, expectedRatio = 1.0),
                            ChoiceInputData(choiceId = "q1c2", responseCount = 6, expectedRatio = 0.6),
                        ),
                        notApplicableCount = 0,
                        expectedSeenRatio = 1.0,
                    ),
                ),
                participantCount = 10,
            ),
            input(
                testDescription = "when 1 question with 1 choice, 1 response and 1 N/A",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 1, expectedRatio = 1.0),
                        ),
                        notApplicableCount = 1,
                        expectedSeenRatio = 0.5,
                    ),
                ),
                participantCount = 2,
            ),
            input(
                testDescription = "when 1 question with multiple choices and some N/A",
                questionDataList = listOf(
                    QuestionInputData(
                        questionId = "question1",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 8, expectedRatio = 1.0),
                            ChoiceInputData(choiceId = "q1c2", responseCount = 4, expectedRatio = 0.5),
                        ),
                        notApplicableCount = 2,
                        expectedSeenRatio = 0.8,
                    ),
                ),
                participantCount = 10,
            ),
        )

        private fun input(
            testDescription: String,
            participantCount: Int,
            questionDataList: List<QuestionInputData>,
        ) = arrayOf(testDescription, participantCount, questionDataList)
    }

    @Test
    fun `getConsultationResults - when has cache - should return cached result`() {
        // Given
        val consultationResults = mock(ConsultationResults::class.java)
        given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
            .willReturn(ConsultationResultsCacheResult.CachedConsultationResults(consultationResults))

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // THen
        assertThat(result).isEqualTo(consultationResults)
        then(consultationInfoRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationResponseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
        then(cacheRepository).should(only()).getConsultationResults(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationResults - when has cacheNotFound - should return null`() {
        // Given
        given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
            .willReturn(ConsultationResultsCacheResult.ConsultationResultsNotFound)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // THen
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationResponseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
        then(cacheRepository).should(only()).getConsultationResults(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationResults - when getConsultation is null - should return null`() {
        // Given
        given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
            .willReturn(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationResponseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
        then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
        then(cacheRepository).should().initConsultationResultsNotFound(consultationId = "consultationId")
        then(cacheRepository).shouldHaveNoMoreInteractions()
    }

    @Nested
    inner class ConsultationUpdatesAndQuestionListNotNullButResponsesEmptyCases {

        private val consultationInfo = mock(ConsultationInfo::class.java)

        @BeforeEach
        fun setUp() {
            given(consultationInfoRepository.getConsultation(consultationId = "consultationId"))
                .willReturn(consultationInfo)
            given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
                .willReturn(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList is empty - should return object without results`() {
            // Given
            given(questionRepository.getConsultationQuestionList(consultationId = "consultationId"))
                .willReturn(emptyList())
            given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
                .willReturn(23)

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            val expectedResults = ConsultationResults(
                consultation = consultationInfo,
                participantCount = 23,
                resultsWithChoices = emptyList(),
                openQuestions = emptyList(),
            )
            assertThat(result).isEqualTo(expectedResults)
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should(only())
                .getParticipantCount(consultationId = "consultationId")
            then(mapper).shouldHaveNoInteractions()
            then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
            then(cacheRepository).should()
                .initConsultationResults(consultationId = "consultationId", results = expectedResults)
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return question but not with choices - should return object without results`() {
            // Given
            val question = mock(QuestionChapter::class.java)
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))
            given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
                .willReturn(77)

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            val expectedResults = ConsultationResults(
                consultation = consultationInfo,
                participantCount = 77,
                resultsWithChoices = emptyList(),
                openQuestions = emptyList(),
            )
            assertThat(result).isEqualTo(expectedResults)
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should(only())
                .getParticipantCount(consultationId = "consultationId")
            then(mapper).shouldHaveNoInteractions()
            then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
            then(cacheRepository).should()
                .initConsultationResults(consultationId = "consultationId", results = expectedResults)
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionWithChoices but empty choices - should return object without result`() {
            // Given
            val question = mock(QuestionWithChoices::class.java).also {
                given(it.choixPossibleList).willReturn(emptyList())
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))
            given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
                .willReturn(1337)

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            val expectedResults = ConsultationResults(
                consultation = consultationInfo,
                participantCount = 1337,
                resultsWithChoices = emptyList(),
                openQuestions = emptyList(),
            )
            assertThat(result).isEqualTo(expectedResults)
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
            then(consultationResponseRepository).should()
                .getConsultationResponsesCount(consultationId = "consultationId")
            then(consultationResponseRepository).shouldHaveNoMoreInteractions()
            then(mapper).shouldHaveNoInteractions()
            then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
            then(cacheRepository).should()
                .initConsultationResults(consultationId = "consultationId", results = expectedResults)
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionWithChoices with choices - should return result with choices with ratio 0`() {
            // Given
            val choixPossible = mock(ChoixPossibleDefault::class.java)
            val question = mock(QuestionUniqueChoice::class.java).also {
                given(it.choixPossibleList).willReturn(listOf(choixPossible))
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))
            given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
                .willReturn(0)
            given(mapper.toQuestionNoResponse(question)).willReturn(question)

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            val expectedResults = ConsultationResults(
                consultation = consultationInfo,
                participantCount = 0,
                resultsWithChoices = listOf(
                    QuestionResults(
                        question = question,
                        seenRatio = 0.0,
                        responses = listOf(
                            ChoiceResults(
                                choixPossible = choixPossible,
                                ratio = 0.0,
                            ),
                        ),
                    ),
                ),
                openQuestions = emptyList(),
            )
            assertThat(result).isEqualTo(expectedResults)
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
            then(consultationResponseRepository).should()
                .getConsultationResponsesCount(consultationId = "consultationId")
            then(consultationResponseRepository).shouldHaveNoMoreInteractions()
            then(mapper).should(only()).toQuestionNoResponse(question)
            then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
            then(cacheRepository).should()
                .initConsultationResults(consultationId = "consultationId", results = expectedResults)
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

    }

    @ParameterizedTest(name = "getConsultationResults / consultationResponseRepository - {0} - should return expected")
    @MethodSource("getConsultationResultsParameters")
    fun `getConsultationResults - when given inputs returned by consultationResponseRepository - should return expected`(
        @Suppress("UNUSED_PARAMETER")
        testDescription: String,
        participantCount: Int,
        inputDataList: List<QuestionInputData>,
    ) {
        // Given
        given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
            .willReturn(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        val consultationInfo = mock(ConsultationInfo::class.java)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultationInfo)

        val testDataList = inputDataList.map(::buildTestData)
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(testDataList.map { it.question })
        given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
            .willReturn(participantCount)
        given(consultationResultAggregatedRepository.getConsultationResultAggregated(consultationId = "consultationId"))
            .willReturn(emptyList())
        given(consultationResponseRepository.getConsultationResponsesCount(consultationId = "consultationId"))
            .willReturn(testDataList.flatMap { it.responsesConsultationList })

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        val expectedResults = ConsultationResults(
            consultation = consultationInfo,
            participantCount = participantCount,
            resultsWithChoices = testDataList.mapNotNull { testData ->
                testData.expectedQuestionResultList?.let {
                    QuestionResults(
                        question = testData.question,
                        seenRatio = testData.expectedSeenRatio,
                        responses = testData.expectedQuestionResultList,
                    )
                }
            },
            openQuestions = emptyList(),
        )
        assertThat(result).isEqualTo(expectedResults)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
        then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
        then(consultationResponseRepository).should(only())
            .getConsultationResponsesCount(consultationId = "consultationId")
        then(consultationResultAggregatedRepository).should(only())
            .getConsultationResultAggregated(consultationId = "consultationId")
        testDataList.filter { it.question.choixPossibleList.isNotEmpty() }.forEach {
            then(mapper).should().toQuestionNoResponse(it.question)
        }
        then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
        then(cacheRepository).should()
            .initConsultationResults(consultationId = "consultationId", results = expectedResults)
        then(cacheRepository).shouldHaveNoMoreInteractions()
    }

    @ParameterizedTest(name = "getConsultationResults / consultationResultAggregatedRepository - {0} - should return expected")
    @MethodSource("getConsultationResultsParameters")
    fun `getConsultationResults - when given inputs returned by consultationResultAggregatedRepository - should return expected`(
        @Suppress("UNUSED_PARAMETER")
        testDescription: String,
        participantCount: Int,
        inputDataList: List<QuestionInputData>,
    ) {
        // Given
        given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
            .willReturn(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        val consultationInfo = mock(ConsultationInfo::class.java)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultationInfo)

        val testDataList = inputDataList.map(::buildTestData)
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(testDataList.map { it.question })
        given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
            .willReturn(participantCount)
        given(consultationResultAggregatedRepository.getConsultationResultAggregated(consultationId = "consultationId"))
            .willReturn(testDataList.flatMap { it.consultationResultAggregated })

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        val expectedResults = ConsultationResults(
            consultation = consultationInfo,
            participantCount = participantCount,
            resultsWithChoices = testDataList.mapNotNull { testData ->
                testData.expectedQuestionResultList?.let {
                    QuestionResults(
                        question = testData.question,
                        seenRatio = testData.expectedSeenRatio,
                        responses = testData.expectedQuestionResultList,
                    )
                }
            },
            openQuestions = emptyList(),
        )
        assertThat(result).isEqualTo(expectedResults)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
        then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
        then(consultationResponseRepository).shouldHaveNoInteractions()
        then(consultationResultAggregatedRepository).should(only())
            .getConsultationResultAggregated(consultationId = "consultationId")
        testDataList.filter { it.question.choixPossibleList.isNotEmpty() }.forEach {
            then(mapper).should().toQuestionNoResponse(it.question)
        }
        then(cacheRepository).should().getConsultationResults(consultationId = "consultationId")
        then(cacheRepository).should()
            .initConsultationResults(consultationId = "consultationId", results = expectedResults)
        then(cacheRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `getConsultationResults - when there is open questions - should return open questions`() {
        // Given
        val questionsOpen = listOf(
            mock(QuestionOpen::class.java),
            mock(QuestionOpen::class.java)
        )

        val consultationInfo = mock(ConsultationInfo::class.java)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultationInfo)
        given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
            .willReturn(8)
        given(consultationResultAggregatedRepository.getConsultationResultAggregated(consultationId = "consultationId"))
            .willReturn(emptyList())
        given(questionRepository.getConsultationQuestionList(consultationId = "consultationId"))
            .willReturn(questionsOpen)
        given(cacheRepository.getConsultationResults(consultationId = "consultationId"))
            .willReturn(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(
            ConsultationResults(
                consultation = consultationInfo,
                participantCount = 8,
                resultsWithChoices = emptyList(),
                openQuestions = questionsOpen
            )
        )
    }

    private fun buildTestData(testInput: QuestionInputData): TestData {
        val choices = testInput.choices.map { choiceInput ->
            mock(ChoixPossibleDefault::class.java).also {
                given(it.id).willReturn(choiceInput.choiceId)
            }
        }

        val question = mock(QuestionWithChoices::class.java).also {
            lenientGiven(it.id).willReturn(testInput.questionId)
            given(it.choixPossibleList).willReturn(choices)
        }

        val (notApplicableResponses, notApplicableAggregatedResponse) = if (testInput.notApplicableCount > 0) {
            mock(ResponseConsultationCount::class.java).also {
                lenientGiven(it.questionId).willReturn(testInput.questionId)
                lenientGiven(it.choiceId).willReturn(UuidUtils.NOT_APPLICABLE_CHOICE_UUID)
                lenientGiven(it.responseCount).willReturn(testInput.notApplicableCount)
            } to mock(ConsultationResultAggregated::class.java).also {
                lenientGiven(it.questionId).willReturn(testInput.questionId)
                lenientGiven(it.choiceId).willReturn(UuidUtils.NOT_APPLICABLE_CHOICE_UUID)
                lenientGiven(it.responseCount).willReturn(testInput.notApplicableCount)
            }
        } else null to null

        val responseConsultationList = testInput.choices.map { choiceInput ->
            mock(ResponseConsultationCount::class.java).also {
                lenientGiven(it.questionId).willReturn(testInput.questionId)
                lenientGiven(it.choiceId).willReturn(choiceInput.choiceId)
                lenientGiven(it.responseCount).willReturn(choiceInput.responseCount)
            }
        } + notApplicableResponses
        val consultationResultAggregated = testInput.choices.map { choiceInput ->
            mock(ConsultationResultAggregated::class.java).also {
                lenientGiven(it.questionId).willReturn(testInput.questionId)
                lenientGiven(it.choiceId).willReturn(choiceInput.choiceId)
                lenientGiven(it.responseCount).willReturn(choiceInput.responseCount)
            }
        } + notApplicableAggregatedResponse

        val expectedQuestionResultList = testInput.choices.map { choiceInput ->
            ChoiceResults(
                choixPossible = choices.find { it.id == choiceInput.choiceId }!!,
                ratio = choiceInput.expectedRatio,
            )
        }.takeIf { it.isNotEmpty() }

        lenientGiven(mapper.toQuestionNoResponse(question)).willReturn(question)
        return TestData(
            question = question,
            responsesConsultationList = responseConsultationList.filterNotNull(),
            consultationResultAggregated = consultationResultAggregated.filterNotNull(),
            expectedQuestionResultList = expectedQuestionResultList,
            expectedSeenRatio = testInput.expectedSeenRatio,
        )
    }

    private data class TestData(
        val question: QuestionWithChoices,
        val responsesConsultationList: List<ResponseConsultationCount>,
        val consultationResultAggregated: List<ConsultationResultAggregated>,
        val expectedQuestionResultList: List<ChoiceResults>?,
        val expectedSeenRatio: Double,
    )

}

internal data class QuestionInputData(
    val questionId: String,
    val choices: List<ChoiceInputData>,
    val notApplicableCount: Int,
    val expectedSeenRatio: Double,
)

internal data class ChoiceInputData(
    val choiceId: String,
    val responseCount: Int,
    val expectedRatio: Double,
)
