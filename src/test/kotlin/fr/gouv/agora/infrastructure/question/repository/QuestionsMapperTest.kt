package fr.gouv.agora.infrastructure.question.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class QuestionsMapperTest {
    private val objectMapper = jacksonObjectMapper()
        .registerKotlinModule()
        .registerModules(JavaTimeModule())

    private lateinit var questionsMapper: QuestionsMapper

    private val questionMapper: QuestionMapper = QuestionMapper()

    @BeforeEach
    fun setUp() {
        questionsMapper = QuestionsMapper(questionMapper)
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
                      "image_de_couverture": {
                        "data": null
                      },
                      "image_page_de_contenu": {
                        "data": null
                      },
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
                            "publishedAt": "2024-05-23T15:48:53.090Z"
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
                          "question_suivante": null,
                          "image": {
                            "data": {
                              "id": 3,
                              "attributes": {
                                "formats": {
                                  "medium": {
                                    "url": "https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/medium_20240215_095657_ef8160b276.jpg"
                                  }
                                }
                              }
                            }
                          }
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
                      },
                      "titre_page_web": "Grande Consultation",
                      "sous_titre_page_web": "par le Gouvernement"
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
