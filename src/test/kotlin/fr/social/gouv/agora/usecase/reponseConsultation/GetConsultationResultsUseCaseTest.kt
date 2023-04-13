package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationRepository
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetReponseConsultationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
    private lateinit var consultationRepository: ConsultationRepository

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
                            ChoiceTestInput(choiceId = "choice1", participationIds = listOf("participationId"), expectedRatio = 1.0),
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
                                expectedRatio = 0.8
                            ),
                            ChoiceTestInput(
                                choiceId = "q2choice2",
                                participationIds = (8 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.2
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
                                expectedRatio = 0.8
                            ),
                            ChoiceTestInput(
                                choiceId = "choice2",
                                participationIds = (8 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.2
                            ),
                        )
                    ),
                    InputData(
                        questionId = "question2",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "choice1",
                                participationIds = (0 until 4).map { index -> "participationId$index" },
                                expectedRatio = 0.4
                            ),
                            ChoiceTestInput(
                                choiceId = "q2choice2",
                                participationIds = (4 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.6
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
                                expectedRatio = 0.8
                            ),
                            ChoiceTestInput(
                                choiceId = "choice2",
                                participationIds = (8 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.2
                            ),
                        )
                    ),
                    InputData(
                        questionId = "question2",
                        choiceInputs = listOf(
                            ChoiceTestInput(
                                choiceId = "choice1",
                                participationIds = (0 until 4).map { index -> "participationId$index" },
                                expectedRatio = 0.4
                            ),
                            ChoiceTestInput(
                                choiceId = "q2choice2",
                                participationIds = (4 until 10).map { index -> "participationId$index" },
                                expectedRatio = 0.6
                            ),
                        )
                    ),
                    InputData(
                        questionId = "question3",
                        choiceInputs = emptyList()
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
                                expectedRatio = 1.0
                            ),
                            ChoiceTestInput(
                                choiceId = "choice2",
                                participationIds = (0 until 6).map { index -> "participationId$index" },
                                expectedRatio = 0.6
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
        given(consultationRepository.getConsultation("consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).shouldHaveNoInteractions()
        then(questionRepository).shouldHaveNoInteractions()
        then(getReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates null - should return null`() {
        // Given
        given(consultationRepository.getConsultation("consultationId")).willReturn(mock(Consultation::class.java))
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).shouldHaveNoInteractions()
        then(getReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates not null, getConsultationQuestionList is empty - should return null`() {
        // Given
        given(consultationRepository.getConsultation("consultationId")).willReturn(mock(Consultation::class.java))
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(mock(ConsultationUpdate::class.java))
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(emptyList())

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
        then(getReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates not null, getConsultationQuestionList not empty with choixPossible, getConsultationResponses empty - should return expected`() {
        // Given
        val consultation = mock(Consultation::class.java)
        given(consultationRepository.getConsultation("consultationId")).willReturn(consultation)
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(consultationUpdate)

        val choixPossible = mock(ChoixPossible::class.java)
        val question = mock(Question::class.java).also {
            given(it.choixPossibleList).willReturn(listOf(choixPossible))
        }
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))
        given(getReponseConsultationRepository.getConsultationResponses("consultationId")).willReturn(emptyList())

        // When
        val result = useCase.getConsultationResults(consultationId = "consultationId")

        // Then
        assertThat(result).isEqualTo(
            ConsultationResult(
                consultation = consultation,
                participantCount = 0,
                results = listOf(
                    QuestionResults(
                        question = question,
                        responses = listOf(
                            QuestionResult(
                                choixPossible = choixPossible,
                                ratio = 0.0,
                            )
                        )
                    )
                ),
                lastUpdate = consultationUpdate,
            )
        )
        then(consultationRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
        then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates not null, getConsultationQuestionList not empty but empty choixPossible, getConsultationResponses empty - should return remove question with empty choixPossible`() {
        // Given
        val consultation = mock(Consultation::class.java)
        given(consultationRepository.getConsultation("consultationId")).willReturn(consultation)
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(consultationUpdate)

        val question = mock(Question::class.java).also {
            given(it.choixPossibleList).willReturn(emptyList())
        }
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))
        given(getReponseConsultationRepository.getConsultationResponses("consultationId")).willReturn(emptyList())

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
        then(consultationRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
        then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
    }

    @Test
    fun `getConsultationResults - when getConsultation not null, getConsultationUpdates not null, getConsultationQuestionList not empty with choixPossible, getConsultationResponses not empty - should return expected`() {
        // Given
        val consultation = mock(Consultation::class.java)
        given(consultationRepository.getConsultation("consultationId")).willReturn(consultation)
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateRepository.getConsultationUpdate("consultationId")).willReturn(consultationUpdate)

        val choixPossible = mock(ChoixPossible::class.java).also {
            given(it.id).willReturn("choixId")
        }
        val question = mock(Question::class.java).also {
            given(it.id).willReturn("questionId")
            given(it.choixPossibleList).willReturn(listOf(choixPossible))
        }
        given(questionRepository.getConsultationQuestionList("consultationId")).willReturn(listOf(question))

        val reponseConsultation = mock(ReponseConsultation::class.java).also {
            given(it.questionId).willReturn("questionId")
            given(it.choiceId).willReturn("choixId")
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
                    QuestionResults(
                        question = question,
                        responses = listOf(
                            QuestionResult(
                                choixPossible = choixPossible,
                                ratio = 1.0,
                            )
                        )
                    )
                ),
                lastUpdate = consultationUpdate,
            )
        )
        then(consultationRepository).should(only()).getConsultation("consultationId")
        then(consultationUpdateRepository).should(only()).getConsultationUpdate("consultationId")
        then(questionRepository).should(only()).getConsultationQuestionList("consultationId")
        then(getReponseConsultationRepository).should(only()).getConsultationResponses("consultationId")
    }

    @ParameterizedTest(name = "getConsultationResults - {0} - should return expected")
    @MethodSource("getConsultationResultsParameters")
    fun `getConsultationResults - when given inputs - should return expected`(
        testDescription: String,
        inputDataList: List<InputData>,
        expectedParticipantCount: Int,
    ) {
        // Given
        val consultation = mock(Consultation::class.java)
        given(consultationRepository.getConsultation("consultationId")).willReturn(consultation)
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
                        QuestionResults(
                            question = testData.question,
                            responses = testData.expectedQuestionResultList
                        )
                    }
                },
                lastUpdate = consultationUpdate,
            )
        )
        then(consultationRepository).should(only()).getConsultation("consultationId")
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

        val question = mock(Question::class.java).also {
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
            QuestionResult(
                choixPossible = choices.find { it.id == choiceInput.choiceId }!!,
                ratio = choiceInput.expectedRatio,
            )
        }.takeIf { it.isNotEmpty() }

        return TestData(question, reponseConsultationList, expectedQuestionResultList)
    }

    private data class TestData(
        val question: Question,
        val reponseConsultationList: List<ReponseConsultation>,
        val expectedQuestionResultList: List<QuestionResult>?,
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