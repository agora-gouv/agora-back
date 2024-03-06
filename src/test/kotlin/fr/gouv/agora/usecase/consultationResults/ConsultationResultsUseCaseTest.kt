package fr.gouv.agora.usecase.consultationResults

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ConsultationResultsUseCaseTest {

    @Autowired
    private lateinit var useCase: ConsultationResultsUseCase

    @MockBean
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @MockBean
    private lateinit var questionRepository: QuestionRepository

    @MockBean
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    @MockBean
    private lateinit var consultationResultAggregatedRepository: ConsultationResultAggregatedRepository

    @MockBean
    private lateinit var mapper: QuestionNoResponseMapper

    @MockBean
    private lateinit var userAnsweredConsultationRepository: UserAnsweredConsultationRepository

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
                        )
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
                        )
                    ),
                    QuestionInputData(
                        questionId = "question2",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q2c1", responseCount = 4, expectedRatio = 0.4),
                            ChoiceInputData(choiceId = "q2c2", responseCount = 6, expectedRatio = 0.6),
                        )
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
                        )
                    ),
                    QuestionInputData(
                        questionId = "question2",
                        choices = listOf(
                            ChoiceInputData(choiceId = "q1c1", responseCount = 4, expectedRatio = 0.4),
                            ChoiceInputData(choiceId = "q2c2", responseCount = 6, expectedRatio = 0.6),
                        )
                    ),
                    QuestionInputData(
                        questionId = "question3",
                        choices = emptyList(),
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
                        )
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
    fun `getConsultationResults - when getConsultation is null - should return null`() {
        // Given
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(questionRepository).shouldHaveNoInteractions()
        then(consultationResponseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Nested
    inner class ConsultationUpdatesAndQuestionListNotNullButResponsesEmptyCases {

        private val consultationInfo = mock(ConsultationInfo::class.java)

        @BeforeEach
        fun setUp() {
            given(consultationInfoRepository.getConsultation(consultationId = "consultationId"))
                .willReturn(consultationInfo)
            given(consultationResponseRepository.getConsultationResponsesCount(consultationId = "consultationId"))
                .willReturn(emptyList())
            given(consultationResultAggregatedRepository.getConsultationResultAggregated(consultationId = "consultationId"))
                .willReturn(emptyList())
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
            assertThat(result).isEqualTo(
                ConsultationResults(
                    consultation = consultationInfo,
                    participantCount = 23,
                    results = emptyList(),
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should(only())
                .getParticipantCount(consultationId = "consultationId")
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return question but not with choices - should return object without results`() {
            // Given
            val question = mock(QuestionOpen::class.java)
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))
            given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
                .willReturn(77)

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResults(
                    consultation = consultationInfo,
                    participantCount = 77,
                    results = emptyList(),
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should(only())
                .getParticipantCount(consultationId = "consultationId")
            then(mapper).shouldHaveNoInteractions()
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
            assertThat(result).isEqualTo(
                ConsultationResults(
                    consultation = consultationInfo,
                    participantCount = 1337,
                    results = emptyList(),
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
            then(consultationResponseRepository).should()
                .getConsultationResponsesCount(consultationId = "consultationId")
            then(consultationResponseRepository).shouldHaveNoMoreInteractions()
            then(mapper).shouldHaveNoInteractions()
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
                .willReturn(1)
            given(mapper.toQuestionNoResponse(question)).willReturn(question)

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResults(
                    consultation = consultationInfo,
                    participantCount = 1,
                    results = listOf(
                        QuestionResults(
                            question = question,
                            responses = listOf(
                                ChoiceResults(
                                    choixPossible = choixPossible,
                                    ratio = 0.0,
                                ),
                            ),
                        ),
                    ),
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
            then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
            then(consultationResponseRepository).should()
                .getConsultationResponsesCount(consultationId = "consultationId")
            then(consultationResponseRepository).shouldHaveNoMoreInteractions()
            then(mapper).should(only()).toQuestionNoResponse(question)
        }

    }

    @ParameterizedTest(name = "getConsultationResults / consultationResponseRepository - {0} - should return expected")
    @MethodSource("getConsultationResultsParameters")
    fun `getConsultationResults - when given inputs returned by consultationResponseRepository - should return expected`(
        testDescription: String,
        participantCount: Int,
        inputDataList: List<QuestionInputData>,
    ) {
        // Given
        val consultationInfo = mock(ConsultationInfo::class.java)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultationInfo)

        val testDataList = inputDataList.map(::buildTestData)
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(testDataList.map { it.question })
        given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
            .willReturn(participantCount)
        given(consultationResponseRepository.getConsultationResponsesCount(consultationId = "consultationId"))
            .willReturn(testDataList.flatMap { it.responsesConsultationList })

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(
            ConsultationResults(
                consultation = consultationInfo,
                participantCount = participantCount,
                results = testDataList.mapNotNull { testData ->
                    testData.expectedQuestionResultList?.let {
                        QuestionResults(
                            question = testData.question,
                            responses = testData.expectedQuestionResultList,
                        )
                    }
                },
            )
        )
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList(consultationId = "consultationId")
        then(userAnsweredConsultationRepository).should().getParticipantCount(consultationId = "consultationId")
        then(consultationResponseRepository).should(only())
            .getConsultationResponsesCount(consultationId = "consultationId")
        then(consultationResultAggregatedRepository).shouldHaveNoInteractions()
        testDataList.filter { it.question.choixPossibleList.isNotEmpty() }.forEach {
            then(mapper).should().toQuestionNoResponse(it.question)
        }
    }

    @ParameterizedTest(name = "getConsultationResults / consultationResultAggregatedRepository - {0} - should return expected")
    @MethodSource("getConsultationResultsParameters")
    fun `getConsultationResults - when given inputs returned by consultationResultAggregatedRepository - should return expected`(
        testDescription: String,
        participantCount: Int,
        inputDataList: List<QuestionInputData>,
    ) {
        // Given
        val consultationInfo = mock(ConsultationInfo::class.java)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultationInfo)
        val consultationUpdate = mock(ConsultationUpdate::class.java)

        val testDataList = inputDataList.map(::buildTestData)
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(testDataList.map { it.question })
        given(userAnsweredConsultationRepository.getParticipantCount(consultationId = "consultationId"))
            .willReturn(participantCount)
        given(consultationResponseRepository.getConsultationResponsesCount(consultationId = "consultationId"))
            .willReturn(emptyList())
        given(consultationResultAggregatedRepository.getConsultationResultAggregated(consultationId = "consultationId"))
            .willReturn(testDataList.flatMap { it.consultationResultAggregated })

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(
            ConsultationResults(
                consultation = consultationInfo,
                participantCount = participantCount,
                results = testDataList.mapNotNull { testData ->
                    testData.expectedQuestionResultList?.let {
                        QuestionResults(
                            question = testData.question,
                            responses = testData.expectedQuestionResultList,
                        )
                    }
                },
            )
        )
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
    }

    private fun buildTestData(testInput: QuestionInputData): TestData {
        val choices = testInput.choices.map { choiceInput ->
            mock(ChoixPossibleDefault::class.java).also {
                given(it.id).willReturn(choiceInput.choiceId)
            }
        }

        val question = mock(QuestionWithChoices::class.java).also {
            given(it.id).willReturn(testInput.questionId)
            given(it.choixPossibleList).willReturn(choices)
        }

        val responseConsultationList = testInput.choices.map { choiceInput ->
            mock(ResponseConsultationCount::class.java).also {
                given(it.questionId).willReturn(testInput.questionId)
                given(it.choiceId).willReturn(choiceInput.choiceId)
                given(it.responseCount).willReturn(choiceInput.responseCount)
            }
        }
        val consultationResultAggregated = testInput.choices.map { choiceInput ->
            mock(ConsultationResultAggregated::class.java).also {
                given(it.questionId).willReturn(testInput.questionId)
                given(it.choiceId).willReturn(choiceInput.choiceId)
                given(it.responseCount).willReturn(choiceInput.responseCount)
            }
        }

        val expectedQuestionResultList = testInput.choices.map { choiceInput ->
            ChoiceResults(
                choixPossible = choices.find { it.id == choiceInput.choiceId }!!,
                ratio = choiceInput.expectedRatio,
            )
        }.takeIf { it.isNotEmpty() }

        given(mapper.toQuestionNoResponse(question)).willReturn(question)
        return TestData(question, responseConsultationList, consultationResultAggregated, expectedQuestionResultList)
    }

    private data class TestData(
        val question: QuestionWithChoices,
        val responsesConsultationList: List<ResponseConsultationCount>,
        val consultationResultAggregated: List<ConsultationResultAggregated>,
        val expectedQuestionResultList: List<ChoiceResults>?,
    )

}

internal data class QuestionInputData(
    val questionId: String,
    val choices: List<ChoiceInputData>,
)

internal data class ChoiceInputData(
    val choiceId: String,
    val responseCount: Int,
    val expectedRatio: Double,
)