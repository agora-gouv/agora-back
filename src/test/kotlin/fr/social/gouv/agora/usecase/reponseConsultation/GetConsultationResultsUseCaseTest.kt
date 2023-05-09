package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetReponseConsultationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
internal class GetConsultationResultsUseCaseTest {

    @Autowired
    private lateinit var useCase: GetConsultationResultsUseCase

    @MockBean
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @MockBean
    private lateinit var questionRepository: QuestionRepository

    @MockBean
    private lateinit var getReponseConsultationRepository: GetReponseConsultationRepository

    @MockBean
    private lateinit var consultationUpdateRepository: ConsultationUpdateRepository

    companion object {
        @JvmStatic
        fun getConsultationResultsParameters() = arrayOf(
            input(
                testDescription = "when 1 question with 1 choice & 1 response",
                inputDataList = listOf(
                    InputData(
                        questionId = "question1",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q1choice1",
                                participationIds = listOf("participationId"),
                                expectedRatio = 1.0,
                            ),
                        ),
                    ),
                ),
                expectedParticipantCount = 1,
            ),
            input(
                testDescription = "when 1 question with 2 choice & some responses",
                inputDataList = listOf(
                    InputData(
                        questionId = "question1",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q1choice1",
                                participationIds = (0 until 8).map { index -> "participationId$index" },
                                expectedRatio = 0.8,
                            ),
                            ChoiceTestInput(
                                choiceId = "q2choice2",
                                participationIds = (8 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.2,
                            ),
                        )
                    ),
                ),
                expectedParticipantCount = 10,
            ),
            input(
                testDescription = "when 2 questions with choices & some responses",
                inputDataList = listOf(
                    InputData(
                        questionId = "question1",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q1choice1",
                                participationIds = (0 until 8).map { index -> "participationId$index" },
                                expectedRatio = 0.8,
                            ),
                            ChoiceTestInput(
                                choiceId = "q1choice2",
                                participationIds = (8 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.2,
                            ),
                        )
                    ),
                    InputData(
                        questionId = "question2",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q2choice1",
                                participationIds = (0 until 4).map { index -> "participationId$index" },
                                expectedRatio = 0.4,
                            ),
                            ChoiceTestInput(
                                choiceId = "q2choice2",
                                participationIds = (4 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.6,
                            ),
                        )
                    ),
                ),
                expectedParticipantCount = 10,
            ),
            input(
                testDescription = "when questions with choices & some responses but some questions do not have choices",
                inputDataList = listOf(
                    InputData(
                        questionId = "question1",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q1choice1",
                                participationIds = (0 until 8).map { index -> "participationId$index" },
                                expectedRatio = 0.8,
                            ),
                            ChoiceTestInput(
                                choiceId = "q1choice2",
                                participationIds = (8 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.2,
                            ),
                        )
                    ),
                    InputData(
                        questionId = "question2",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q1choice1",
                                participationIds = (0 until 4).map { index -> "participationId$index" },
                                expectedRatio = 0.4,
                            ),
                            ChoiceTestInput(
                                choiceId = "q2choice2",
                                participationIds = (4 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.6,
                            ),
                        )
                    ),
                    InputData(
                        questionId = "question3",
                        choiceInputs = emptyList(),
                    ),
                ),
                expectedParticipantCount = 10,
            ),
            input(
                testDescription = "when has question with multiple choices",
                inputDataList = listOf(
                    InputData(
                        questionId = "question1",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "q1choice1",
                                participationIds = (0 until 10).map { index -> "participationId$index" },
                                expectedRatio = 1.0,
                            ),
                            ChoiceTestInput(
                                choiceId = "q1choice2",
                                participationIds = (0 until 6).map { index -> "participationId$index" },
                                expectedRatio = 0.6,
                            ),
                        )
                    ),
                ),
                expectedParticipantCount = 10,
            ),
        )

        private fun input(
            testDescription: String,
            inputDataList: List<InputData>,
            expectedParticipantCount: Int,
        ) = arrayOf(testDescription, inputDataList, expectedParticipantCount)
    }

    @Test
    fun `getConsultationResults - when getConsultation is null - should return null`() {
        // Given
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(getReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates null - should return null`() {
        // Given
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(mock(ConsultationInfo::class.java))
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).shouldHaveNoInteractions()
        then(getReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates not null, getConsultationQuestionList is empty - should return null`() {
        // Given
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(mock(ConsultationInfo::class.java))
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(mock(ConsultationUpdate::class.java))
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(emptyList())

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
        then(getReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Nested
    inner class ConsultationUpdatesAndQuestionListNotNullButResponsesEmptyCases {

        private val consultation = mock(ConsultationInfo::class.java)
        private val consultationUpdate = mock(ConsultationUpdate::class.java)

        @BeforeEach
        fun setUp() {
            given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultation)
            given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(consultationUpdate)
            given(getReponseConsultationRepository.getConsultationResponses("consultationId")).willReturn(emptyList())
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionChoixUnique without choices - should return object without result`() {
            // Given
            val question = mock(QuestionChoixUnique::class.java).also {
                given(it.choixPossibleList).willReturn(emptyList())
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 0,
                    results = emptyList(),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionChoixUnique - should return result with choices with ratio 0`() {
            // Given
            val choixPossible = mock(ChoixPossible::class.java)
            val question = mock(QuestionChoixUnique::class.java).also {
                given(it.choixPossibleList).willReturn(listOf(choixPossible))
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 0,
                    results = listOf(
                        QuestionResult(
                            question = question,
                            responses = listOf(
                                ChoiceResult(
                                    choixPossible = choixPossible,
                                    ratio = 0.0,
                                ),
                            ),
                        ),
                    ),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionChoixMultiple without choices - should return object without result`() {
            // Given
            val question = mock(QuestionChoixMultiple::class.java).also {
                given(it.choixPossibleList).willReturn(emptyList())
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 0,
                    results = emptyList(),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionChoixMultiple - should return result with choices with ratio 0`() {
            // Given
            val choixPossible = mock(ChoixPossible::class.java)
            val question = mock(QuestionChoixMultiple::class.java).also {
                given(it.choixPossibleList).willReturn(listOf(choixPossible))
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 0,
                    results = listOf(
                        QuestionResult(
                            question = question,
                            responses = listOf(
                                ChoiceResult(
                                    choixPossible = choixPossible,
                                    ratio = 0.0,
                                ),
                            ),
                        ),
                    ),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionOuverte - should return object without results`() {
            // Given
            val question = mock(QuestionOuverte::class.java)
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 0,
                    results = emptyList(),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return chapter - should return object without results`() {
            // Given
            val question = mock(Chapitre::class.java)
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 0,
                    results = emptyList(),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

    }

    @Nested
    inner class ConsultationUpdatesAndQuestionListNotNullWithResponsesCases {

        private val consultation = mock(ConsultationInfo::class.java)
        private val consultationUpdate = mock(ConsultationUpdate::class.java)

        @BeforeEach
        fun setUp() {
            given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultation)
            given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(consultationUpdate)
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionChoixUnique - should return expected`() {
            // Given
            val choixPossible = mock(ChoixPossible::class.java).also {
                given(it.id).willReturn("choiceId1")
            }
            val question = mock(QuestionChoixUnique::class.java).also {
                given(it.id).willReturn("questionId1")
                given(it.choixPossibleList).willReturn(listOf(choixPossible))
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            val reponseConsultation = mock(ReponseConsultation::class.java).also {
                given(it.questionId).willReturn("questionId1")
                given(it.choiceId).willReturn("choiceId1")
                given(it.participationId).willReturn("participationId1")
            }
            given(getReponseConsultationRepository.getConsultationResponses("consultationId"))
                .willReturn(listOf(reponseConsultation))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 1,
                    results = listOf(
                        QuestionResult(
                            question = question,
                            responses = listOf(
                                ChoiceResult(
                                    choixPossible = choixPossible,
                                    ratio = 1.0,
                                ),
                            ),
                        ),
                    ),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }

        @Test
        fun `getConsultationResults - when getConsultationQuestionList return questionChoixMultiple - should return expected`() {
            // Given
            val choixPossible1 = mock(ChoixPossible::class.java).also {
                given(it.id).willReturn("choiceId1")
            }
            val choixPossible2 = mock(ChoixPossible::class.java).also {
                given(it.id).willReturn("choiceId2")
            }
            val question = mock(QuestionChoixMultiple::class.java).also {
                given(it.id).willReturn("questionId1")
                given(it.choixPossibleList).willReturn(listOf(choixPossible1, choixPossible2))
            }
            given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

            val reponseConsultation = mock(ReponseConsultation::class.java).also {
                given(it.questionId).willReturn("questionId1")
                given(it.choiceId).willReturn("choiceId1")
                given(it.participationId).willReturn("participationId1")
            }
            given(getReponseConsultationRepository.getConsultationResponses("consultationId"))
                .willReturn(listOf(reponseConsultation))

            // When
            val result = useCase.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(
                ConsultationResult(
                    consultation = consultation,
                    participantCount = 1,
                    results = listOf(
                        QuestionResult(
                            question = question,
                            responses = listOf(
                                ChoiceResult(
                                    choixPossible = choixPossible1,
                                    ratio = 1.0,
                                ),
                                ChoiceResult(
                                    choixPossible = choixPossible2,
                                    ratio = 0.0,
                                ),
                            ),
                        ),
                    ),
                    lastUpdate = consultationUpdate,
                )
            )
            then(consultationInfoRepository).should(only()).getConsultation("consultationId")
            then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
            then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
            then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
        }
    }

    @ParameterizedTest(name = "getConsultationResults - {0} - should return expected")
    @MethodSource("getConsultationResultsParameters")
    fun `getConsultationResults - when given inputs - should return expected`(
        testDescription: String,
        inputDataList: List<InputData>,
        expectedParticipantCount: Int,
    ) {
        // Given
        val consultation = mock(ConsultationInfo::class.java)
        given(consultationInfoRepository.getConsultation("consultationId")).willReturn(consultation)
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(consultationUpdate)

        val testDataList = inputDataList.map(::buildTestData)
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(testDataList.map { it.question })
        given(getReponseConsultationRepository.getConsultationResponses("consultationId")).willReturn(testDataList.flatMap { it.reponseConsultationList })

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(
            ConsultationResult(
                consultation = consultation,
                participantCount = expectedParticipantCount,
                results = testDataList.mapNotNull { testData ->
                    testData.expectedQuestionResultList?.let {
                        QuestionResult(
                            question = testData.question,
                            responses = testData.expectedQuestionResultList,
                        )
                    }
                },
                lastUpdate = consultationUpdate,
            )
        )
        then(consultationInfoRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
        then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
    }

    private fun buildTestData(testInput: InputData): TestData {
        val choices = testInput.choiceInputs.map { choiceInput ->
            mock(ChoixPossible::class.java).also {
                given(it.id).willReturn(choiceInput.choiceId)
            }
        }

        val question = mock(QuestionWithChoices::class.java).also {
            given(it.id).willReturn(testInput.questionId)
            given(it.choixPossibleList).willReturn(choices)
        }

        val reponseConsultationList = testInput.choiceInputs.flatMap { choiceInput ->
            choiceInput.participationIds.map { participationId ->
                mock(ReponseConsultation::class.java).also {
                    given(it.questionId).willReturn(testInput.questionId)
                    given(it.choiceId).willReturn(choiceInput.choiceId)
                    given(it.participationId).willReturn(participationId)
                }
            }
        }

        val expectedQuestionResultList = testInput.choiceInputs.map { choiceInput ->
            ChoiceResult(
                choixPossible = choices.find { it.id == choiceInput.choiceId }!!,
                ratio = choiceInput.expectedRatio,
            )
        }.takeIf { it.isNotEmpty() }

        return TestData(question, reponseConsultationList, expectedQuestionResultList)
    }

    private data class TestData(
        val question: Question,
        val reponseConsultationList: List<ReponseConsultation>,
        val expectedQuestionResultList: List<ChoiceResult>?,
    )

}

internal data class InputData(
    val questionId: String,
    val choiceInputs: List<ChoiceTestInput>,
)

internal data class ChoiceTestInput(
    val choiceId: String,
    val participationIds: List<String>,
    val expectedRatio: Double,
)