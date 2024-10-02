package fr.gouv.agora.infrastructure.question.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.domain.ChoixPossibleConditional
import fr.gouv.agora.domain.ChoixPossibleDefault
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class QuestionsMapperTest {
    private val objectMapper = jacksonObjectMapper()
        .registerKotlinModule()
        .registerModules(JavaTimeModule())

    private lateinit var questionsMapper: QuestionsMapper

    private val choixPossibleMapper: ChoixPossibleMapper = mock(ChoixPossibleMapper::class.java)

    private val questionMapper: QuestionMapper = QuestionMapper()

    @BeforeEach
    fun setUp() {
        questionsMapper = QuestionsMapper(choixPossibleMapper, questionMapper)
    }

    private val questionUUID = UUID.randomUUID()
    private val consultationUUID = UUID.randomUUID()

    private val questionDTO = QuestionDTO(
        id = questionUUID,
        title = "title",
        popupDescription = "popupDescription",
        ordre = 1,
        type = "unknown",
        description = null,
        maxChoices = null,
        nextQuestionId = null,
        consultationId = consultationUUID,
    )

    @Nested
    inner class UniqueChoiceTestCases {

        private val questionUniqueChoiceDTO = questionDTO.copy(type = "unique")

        private val expectedQuestion = QuestionUniqueChoice(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            choixPossibleList = emptyList(),
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionUniqueChoice with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionUniqueChoiceDTO = questionUniqueChoiceDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionUniqueChoice with null nextQuestionId`() {
            // Given
            val questionUniqueChoiceDTO = questionUniqueChoiceDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionUniqueChoice with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionUniqueChoiceDTO = questionUniqueChoiceDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionUniqueChoiceDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = listOf(questionUniqueChoiceDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList - should return QuestionUniqueChoice with mapped choixPossible`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            val choixPossible = mock(ChoixPossibleDefault::class.java)
            given(choixPossibleMapper.toDefault(choixPossibleDTO)).willReturn(choixPossible)

            // When
            val result = questionsMapper.toDomain(
                dto = questionUniqueChoiceDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = listOf(choixPossible)))
        }

    }

    @Nested
    inner class MultipleChoicesTestCases {

        private val questionMultipleChoicesDTO = questionDTO.copy(type = "multiple", maxChoices = 5)

        private val expectedQuestion = QuestionMultipleChoices(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            choixPossibleList = emptyList(),
            maxChoices = 5,
        )

        @Test
        fun `toDomain - when has null maxChoices - should throw exception`() {
            // Given
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(maxChoices = null)

            // When
            val hasException = try {
                questionsMapper.toDomain(
                    dto = questionMultipleChoicesDTO,
                    questionDTOList = emptyList(),
                    choixPossibleDTOList = emptyList(),
                )
                false
            } catch (e: Exception) {
                true
            }

            // Then
            assertThat(hasException).isEqualTo(true)
        }

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionMultipleChoices with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionMultipleChoices with null nextQuestionId`() {
            // Given
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionMultipleChoices with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionMultipleChoicesDTO = questionMultipleChoicesDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionMultipleChoicesDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = listOf(questionMultipleChoicesDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList - should return QuestionMultipleChoices with mapped choixPossible`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            val choixPossible = mock(ChoixPossibleDefault::class.java)
            given(choixPossibleMapper.toDefault(choixPossibleDTO)).willReturn(choixPossible)

            // When
            val result = questionsMapper.toDomain(
                dto = questionMultipleChoicesDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = listOf(choixPossible)))
        }

    }

    @Nested
    inner class OpenTestCases {

        private val questionOpenDTO = questionDTO.copy(type = "open")

        private val expectedQuestion = QuestionOpen(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionOpen with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionOpenDTO = questionOpenDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionOpenDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionOpen with null nextQuestionId`() {
            // Given
            val questionOpenDTO = questionOpenDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionOpenDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionOpen with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionOpenDTO = questionOpenDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionOpenDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionOpenDTO,
                questionDTOList = listOf(questionOpenDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

    }

    @Nested
    inner class ChapterTestCases {

        private val questionChapterDTO = questionDTO.copy(type = "chapter", description = "description")

        private val expectedQuestion = QuestionChapter(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            description = "description",
            urlImage = null,
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionChapter with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionChapterDTO = questionChapterDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionChapterDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionChapter with null nextQuestionId`() {
            // Given
            val questionChapterDTO = questionChapterDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionChapterDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionChapter with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionChapterDTO = questionChapterDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionChapterDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionChapterDTO,
                questionDTOList = listOf(questionChapterDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

    }

    @Nested
    inner class ConditionalChoiceTestCases {

        private val questionConditionalDTO = questionDTO.copy(type = "conditional")

        private val expectedQuestion = QuestionConditional(
            id = questionUUID.toString(),
            title = "title",
            popupDescription = "popupDescription",
            order = 1,
            nextQuestionId = null,
            consultationId = consultationUUID.toString(),
            choixPossibleList = emptyList(),
        )

        @Test
        fun `toDomain - when has non null nextQuestionId - should return QuestionConditional with nextQuestionId`() {
            // Given
            val nextQuestionUUID = UUID.randomUUID()
            val questionConditionalDTO = questionConditionalDTO.copy(nextQuestionId = nextQuestionUUID)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has no next question in list - should return QuestionConditional with null nextQuestionId`() {
            // Given
            val questionConditionalDTO = questionConditionalDTO.copy(nextQuestionId = null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = null))
        }

        @Test
        fun `toDomain - when has null nextQuestionId and questionList has next question in list - should return QuestionConditional with nextQuestionId equal to id of question with ordre 2`() {
            // Given
            val questionConditionalDTO = questionConditionalDTO.copy(nextQuestionId = null)
            val nextQuestionUUID = UUID.randomUUID()
            val nextQuestionDTO = questionConditionalDTO.copy(
                id = nextQuestionUUID,
                ordre = 2,
            )

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = listOf(questionConditionalDTO, nextQuestionDTO),
                choixPossibleDTOList = emptyList(),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(nextQuestionId = nextQuestionUUID.toString()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList but mapped to null - should return QuestionConditional empty choixPossibleList`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            given(choixPossibleMapper.toConditional(choixPossibleDTO)).willReturn(null)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = emptyList()))
        }

        @Test
        fun `toDomain - when has choixPossibleDTOList mapped to an object - should return QuestionConditional with mapped choixPossible`() {
            // Given
            val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
            val choixPossible = mock(ChoixPossibleConditional::class.java)
            given(choixPossibleMapper.toConditional(choixPossibleDTO)).willReturn(choixPossible)

            // When
            val result = questionsMapper.toDomain(
                dto = questionConditionalDTO,
                questionDTOList = emptyList(),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )

            // Then
            assertThat(result).isEqualTo(expectedQuestion.copy(choixPossibleList = listOf(choixPossible)))
        }

    }

    @Nested
    inner class StrapiQuestionsMapperTest {
        @Test
        fun `it map Strapi's questions`() {
            // given
            @Language("JSON")
            val consultationAvecTousTypesDeQuestionJson = """
                {
                    "id": 6,
                    "attributes": {
                      "slug": "",
                      "url_image_de_couverture": "",
                      "url_image_page_de_contenu": "",
                      "nombre_de_questions": 5,
                      "estimation_nombre_de_questions": "5",
                      "estimation_temps": "5 minutes",
                      "nombre_participants_cible": 5000,
                      "createdAt": "2024-07-30T12:56:08.853Z",
                      "updatedAt": "2024-07-30T13:32:28.132Z",
                      "publishedAt": "2024-07-30T12:56:28.553Z",
                      "datetime_de_debut": "2024-07-22T22:00:00.000Z",
                      "datetime_de_fin": "2038-07-14T22:00:00.000Z",
                      "titre_consultation": "Test questions",
                      "territoire": "Nord",
                      "thematique": {
                        "data": {
                          "id": 17,
                          "attributes": {
                            "label": "Autonomie",
                            "pictogramme": "👵",
                            "createdAt": "2024-05-23T15:48:53.092Z",
                            "updatedAt": "2024-05-23T15:48:53.092Z",
                            "publishedAt": "2024-05-23T15:48:53.090Z",
                            "id_base_de_donnees": "3953a966-015e-11ee-be56-0242ac120002"
                          }
                        }
                      },
                      "questions": [
                        {
                          "id": 10004,
                          "__component": "question-de-consultation.description",
                          "titre": "Description",
                          "numero": 1,
                          "description": [],
                          "question_suivante": null
                        },
                        {
                          "id": 20002,
                          "__component": "question-de-consultation.question-a-choix-multiples",
                          "titre": "Question à choix multiples",
                          "numero": 2,
                          "nombre_maximum_de_choix": 1,
                          "popup_explication": null,
                          "question_suivante": null,
                          "choix": [
                            {
                              "id": 80,
                              "label": "choix multiple 1",
                              "ouvert": false
                            },
                            {
                              "id": 81,
                              "label": "choix multiple 2 ouvert",
                              "ouvert": true
                            }
                          ]
                        },
                        {
                          "id": 30002,
                          "__component": "question-de-consultation.question-a-choix-unique",
                          "titre": "question à choix unique",
                          "numero": 3,
                          "popup_explication": null,
                          "question_suivante": null,
                          "choix": [
                            {
                              "id": 82,
                              "label": "choix 1",
                              "ouvert": false
                            },
                            {
                              "id": 83,
                              "label": "choix 2 ouvert",
                              "ouvert": true
                            }
                          ]
                        },
                        {
                          "id": 40002,
                          "__component": "question-de-consultation.question-conditionnelle",
                          "titre": "question conditionnelle",
                          "numero": 4,
                          "popup_explication": null,
                          "choix": [
                            {
                              "id": 7,
                              "label": "vers 5",
                              "numero_de_la_question_suivante": 5,
                              "ouvert": false
                            },
                            {
                              "id": 8,
                              "label": "vers 6",
                              "numero_de_la_question_suivante": 6,
                              "ouvert": false
                            }
                          ]
                        },
                        {
                          "id": 50004,
                          "__component": "question-de-consultation.question-ouverte",
                          "titre": "question ouverte",
                          "numero": 5,
                          "popup_explication": null,
                          "question_suivante": 7
                        },
                        {
                          "id": 50006,
                          "__component": "question-de-consultation.question-ouverte",
                          "titre": "question ouverte",
                          "numero": 6,
                          "popup_explication": null,
                          "question_suivante": 7
                        },
                        {
                          "id": 50005,
                          "__component": "question-de-consultation.question-ouverte",
                          "titre": "question finale",
                          "numero": 7,
                          "popup_explication": null,
                          "question_suivante": null
                        }
                      ],
                      "consultation_avant_reponse": {
                        "data": {
                          "id": 6003,
                          "attributes": {
                            "slug": "",
                            "template_partage": " ",
                            "historique_titre": " ",
                            "historique_call_to_action": " ",
                            "createdAt": "2024-07-30T14:48:10.811Z",
                            "updatedAt": "2024-07-30T14:49:13.946Z",
                            "sections": [],
                            "commanditaire": [],
                            "objectif": [],
                            "axe_gouvernemental": [],
                            "presentation": []
                          }
                        }
                      },
                      "consultation_apres_reponse_ou_terminee": {
                        "data": {
                          "id": 12003,
                          "attributes": {
                            "historique_titre": "",
                            "historique_call_to_action": "",
                            "slug":  "",
                            "feedback_message": "",
                            "nom_strapi":  "",
                            "template_partage": "",
                            "sections":  [],
                            "createdAt": "2024-07-30T14:48:56.580Z",
                            "updatedAt": "2024-07-30T14:49:17.531Z"
                          }
                        }
                      },
                      "consultation_contenu_analyse_des_reponse": {
                        "data": null
                      },
                      "contenu_reponse_du_commanditaires": {
                        "data": null
                      },
                      "consultation_contenu_autres": {
                        "data": []
                      },
                      "consultation_contenu_a_venir": {
                        "data": null
                      }
                    }
                  }
            """.trimIndent()

            val ref = object : TypeReference<StrapiAttributes<ConsultationStrapiDTO>>() {}

            val consultationAvecTousTypesDeQuestion = objectMapper
                .readValue(consultationAvecTousTypesDeQuestionJson, ref)

            // when
            val actual = questionsMapper.toDomain(consultationAvecTousTypesDeQuestion)

            // then
            assertThat(actual.filterIsInstance<QuestionConditional>()).hasSize(1)
            assertThat(actual.filterIsInstance<QuestionMultipleChoices>()).hasSize(1)
            assertThat(actual.filterIsInstance<QuestionChapter>()).hasSize(1)
            assertThat(actual.filterIsInstance<QuestionUniqueChoice>()).hasSize(1)
            assertThat(actual.filterIsInstance<QuestionOpen>()).hasSize(3)
        }
    }
}
