package fr.gouv.agora.infrastructure.consultation.dto

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.infrastructure.common.StrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class ConsultationStrapiDTOTest {
    private val objectMapper = jacksonObjectMapper()
        .registerKotlinModule()
        .registerModules(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Test
    fun `parse ConsultationStrapiDTO`() {
        // GIVEN
        @Language("JSON")
        val jsonConsultation = """
            {
              "data": [
                {
                  "id": 2,
                  "attributes": {
                    "url_image_de_couverture": "https://content.agora.beta.gouv.fr/consultation_covers/agriculteurs_consommateurs.jpg",
                    "url_image_page_de_contenu": "https://content.agora.beta.gouv.fr/consultation_covers/agriculteurs_consommateurs.jpg",
                    "nombre_de_questions": 8,
                    "estimation_nombre_de_questions": "8 questions",
                    "estimation_temps": "5 minutes",
                    "nombre_participants_cible": 5000,
                    "createdAt": "2024-06-21T12:56:18.914Z",
                    "updatedAt": "2024-07-03T12:48:11.644Z",
                    "publishedAt": "2024-06-21T13:22:52.004Z",
                    "flamme_label": "Gogogo !",
                    "datetime_de_debut": "2024-06-25T23:00:00.000Z",
                    "datetime_de_fin": "2025-06-26T04:45:00.000Z",
                    "description": [
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "Le 22 février 2024, le Premier ministre Gabriel Attal a désigné Anne-Laure Babault et Alexis Izard pour réaliser une mission parlementaire afin d’évaluer une potentielle évolution des lois EGAlim et, plus globalement, des négociations commerciales dans l’agriculture et l’alimentation. ",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "Les États généraux de l'alimentation (EGAlim), réunis en 2017, répondaient à un engagement fort du Président de la République en faveur d’une alimentation saine et durable et de relations commerciales plus équilibrées entre la grande distribution, les industriels et les agriculteurs. Ils ont offert un temps de réflexion partagée pour construire des solutions nouvelles. ",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "Ce processus a abouti à la loi du 30 octobre 2018 pour l'équilibre des relations commerciales dans le secteur agricole et alimentaire et une alimentation saine, durable et accessible à tous, dite « EGAlim 1 ». Elle a été complétée ensuite par la loi du 18 octobre 2021 visant à protéger la rémunération des agriculteurs, dite « EGAlim 2 ». Ces lois ont placé les agriculteurs au cœur de la construction des prix afin de permettre de créer un lien direct entre leurs coûts de production et le prix payé en magasin.",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "En dépit de résultats positifs, des adaptations de ces lois sont nécessaires pour les rendre plus simples, plus efficaces et plus protectrices des agriculteurs.",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "Vos réponses à cette consultation permettront à la mission de recueillir votre avis, en tant que consommateur, sur les enjeux de meilleure rémunération des agriculteurs et de transparence dans ce secteur, en lien avec l’objectif de reconquête de notre souveraineté alimentaire. Vos contributions nourriront les propositions de réforme des lois EGAlim. Une autre consultation dédiée est disponible dans l’application pour les professionnels de ce secteur.",
                            "type": "text"
                          }
                        ]
                      }
                    ],
                    "objectifs": [
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "👀 Objectif : !",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "paragraph",
                        "children": [
                          {
                            "text": "🌵 Cactus !",
                            "type": "text"
                          }
                        ]
                      }
                    ],
                    "titre_consultation": "Mieux rémunérer les agriculteurs : quels leviers pour le consommateur ?",
                    "thematique": {
                      "data": {
                        "id": 11,
                        "attributes": {
                          "label": "Agriculture & alimentation",
                          "pictogramme": "🌾",
                          "createdAt": "2024-05-23T15:48:53.048Z",
                          "updatedAt": "2024-05-23T15:48:53.048Z",
                          "publishedAt": "2024-05-23T15:48:53.047Z",
                          "id_base_de_donnees": "5cdb4732-0153-11ee-be56-0242ac120002"
                        }
                      }
                    },
                    "questions": [
                      {
                        "id": 2,
                        "__component": "question-de-consultation.question-a-choix-multiples",
                        "titre": "Que priorisez-vous dans l’achat d’un produit alimentaire ?",
                        "numero": 1,
                        "nombre_maximum_de_choix": 3,
                        "popup_explication": null
                      },
                      {
                        "id": 3,
                        "__component": "question-de-consultation.question-a-choix-unique",
                        "titre": "Quelle est la première raison de votre choix de lieu de courses ?",
                        "numero": 2,
                        "popup_explication": null
                      },
                      {
                        "id": 4,
                        "__component": "question-de-consultation.question-a-choix-unique",
                        "titre": "La présence d’un label (Label rouge, Bio, AOP) sur un produit alimentaire vous rassure-t-elle quant à la rémunération de l’agriculteur ?",
                        "numero": 3,
                        "popup_explication": null
                      },
                      {
                        "id": 2,
                        "__component": "question-de-consultation.question-ouverte",
                        "titre": "Qu’est-ce qui devrait être amélioré pour mieux vous informer sur la rémunération de l’agriculteur ?",
                        "numero": 4,
                        "popup_explication": null
                      },
                      {
                        "id": 3,
                        "__component": "question-de-consultation.question-a-choix-multiples",
                        "titre": "A quelles conditions seriez-vous prêt(e) à payer plus cher vos produits alimentaires pour mieux rémunérer les agriculteurs ? ",
                        "numero": 5,
                        "nombre_maximum_de_choix": 3,
                        "popup_explication": null
                      },
                      {
                        "id": 4,
                        "__component": "question-de-consultation.question-a-choix-multiples",
                        "titre": "Quels produits seriez-vous prêt(e) à payer un peu plus cher pour mieux rémunérer les agriculteurs ?",
                        "numero": 6,
                        "nombre_maximum_de_choix": 3,
                        "popup_explication": null
                      },
                      {
                        "id": 2,
                        "__component": "question-de-consultation.description",
                        "titre": "A quoi servent les lois EGAlim ?",
                        "numero": 7,
                        "description": [
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "La loi EGAlim a constitué une avancée importante pour une meilleure répartition de la valeur entre agriculteurs, industriels et distributeurs. Elle a notamment permis d’inscrire dans les pratiques de nouveaux modes de négociation en ",
                                "type": "text"
                              },
                              {
                                "bold": true,
                                "text": "inversant la construction du prix",
                                "type": "text"
                              },
                              {
                                "text": " et d’",
                                "type": "text"
                              },
                              {
                                "bold": true,
                                "text": "encadrer précisément les modalités et le contenu de la contractualisation écrite.",
                                "type": "text"
                              },
                              {
                                "text": " ",
                                "type": "text"
                              }
                            ]
                          },
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "",
                                "type": "text"
                              }
                            ]
                          },
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "La loi EGAlim 2 vise à renforcer la logique de construction du prix des produits alimentaires « en marche avant », c’est-à-dire ",
                                "type": "text"
                              },
                              {
                                "bold": true,
                                "text": "à partir des coûts de production des agriculteurs.",
                                "type": "text"
                              },
                              {
                                "text": " Ces coûts doivent être répercutés tout au long de la chaîne de production, jusqu’à la transformation et la commercialisation de ces produits.",
                                "type": "text"
                              }
                            ]
                          }
                        ]
                      },
                      {
                        "id": 5,
                        "__component": "question-de-consultation.question-a-choix-multiples",
                        "titre": "Selon vous, pour garantir la rémunération des agriculteurs, il serait préférable de :",
                        "numero": 8,
                        "nombre_maximum_de_choix": 3,
                        "popup_explication": null
                      },
                      {
                        "id": 3,
                        "__component": "question-de-consultation.question-ouverte",
                        "titre": "Que recommanderiez-vous à la mission parlementaire pour améliorer le revenu des agriculteurs ?",
                        "numero": 9,
                        "popup_explication": null
                      }
                    ],
                    "consultation_avant_reponse": {
                      "data": {
                        "id": 1,
                        "attributes": {
                          "template_partage": "Comme moi, tu peux participer à la consultation : {title} {url}",
                          "historique_titre": "Lancement",
                          "historique_call_to_action": "Voir les objectifs",
                          "historique_type": "contenu",
                          "createdAt": "2024-06-21T12:59:00.544Z",
                          "updatedAt": "2024-07-02T10:00:56.724Z",
                          "publishedAt": "2024-06-21T12:59:01.521Z",
                          "datetime_publication": "2024-07-01T23:00:00.000Z"
                        }
                      }
                    },
                    "consultation_apres_reponse_ou_terminee": {
                      "data": {
                        "id": 1,
                        "attributes": {
                          "template_partage_avant_fin_consultation": "Comme moi, tu peux participer à la consultation : {title} {url}",
                          "template_partage_apres_fin_consultation": "Les premiers résultats de la consultation {title} : {url}",
                          "footer_titre": "Envie d'aller plus loin ?",
                          "footer_description": [
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "- Cliquez ici pour en savoir plus sur la mission parlementaire",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "- Cliquez ici pour mieux comprendre la loi EGAlim 2  ",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "- Cliquez ici pour en savoir plus sur la construction des prix des produits alimentaires ",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "Si vous souhaitez prendre connaissance des textes de référence :",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "- Cliquez ici pour lire la loi EGAlim 1",
                                  "type": "text"
                                }
                              ]
                            },
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "- Cliquez ici pour lire la loi EGAlim 2",
                                  "type": "text"
                                }
                              ]
                            }
                          ],
                          "feedback_pictogramme": "👀",
                          "feedback_titre": "titre feedback",
                          "feedback_description": [
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "description ",
                                  "type": "text"
                                },
                                {
                                  "bold": true,
                                  "text": "feedback",
                                  "type": "text"
                                }
                              ]
                            }
                          ],
                          "historique_titre": "Fin de consultation",
                          "historique_call_to_action": "Consulter toutes les réponses",
                          "historique_type": "réponse et pourcentage",
                          "createdAt": "2024-06-21T14:23:51.194Z",
                          "updatedAt": "2024-07-02T10:01:15.907Z",
                          "publishedAt": "2024-06-21T14:23:52.266Z",
                          "encart_visualisation_resultat_avant_fin_consultation_picto": "🙌",
                          "encart_visualisation_resultat_avant_fin_consultation_desc": [
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "Merci pour votre participation à cette consultation !",
                                  "type": "text"
                                }
                              ]
                            }
                          ],
                          "encart_visualisation_resultat_avant_fin_consultation_cta": "Voir tous les résultats",
                          "encart_visualisation_resultat_apres_fin_consultation_picto": "🏁",
                          "encart_visualisation_resultat_apres_fin_consultation_desc": [
                            {
                              "type": "paragraph",
                              "children": [
                                {
                                  "text": "Cette consultation est maintenant terminée. Les résultats sont en cours d'analyse. Vous serez notifié(e) dès que la synthèse sera disponible.",
                                  "type": "text"
                                }
                              ]
                            }
                          ],
                          "encart_visualisation_resultat_apres_fin_consultation_cta": "Voir les premiers résultats",
                          "datetime_publication": "2024-06-24T22:30:00.000Z"
                        }
                      }
                    },
                    "consultation_contenu_autres": {
                      "data": [
                        {
                          "id": 1,
                          "attributes": {
                            "message_mise_a_jour": "Analyse disponible",
                            "lien_telechargement_analyse": "https://content.agora.incubateur.net/consultation-syntheses/20240605-agora_handicap_synthese_VF.pdf",
                            "template_partage": "Cela peut t'intéresser : l'analyse des réponses de la consultation {title} {url}",
                            "header_titre": "Analyse des réponses",
                            "header_description": [
                              {
                                "type": "paragraph",
                                "children": [
                                  {
                                    "text": "Les participants souhaitent en majorité financer la transition écologique tout en réduisant la dette publique.",
                                    "type": "text"
                                  }
                                ]
                              }
                            ],
                            "footer_titre": "optionnel normalement",
                            "footer_description": [
                              {
                                "type": "paragraph",
                                "children": [
                                  {
                                    "text": "optionnel normalement",
                                    "type": "text"
                                  }
                                ]
                              }
                            ],
                            "feedback_pictogramme": "💬",
                            "feedback_titre": "Donnez votre avis",
                            "feedback_description": [
                              {
                                "type": "paragraph",
                                "children": [
                                  {
                                    "text": "Êtes-vous satisfait(e) de l'analyse de cette consultation ?",
                                    "type": "text"
                                  }
                                ]
                              }
                            ],
                            "historique_titre": "Analyse des réponses",
                            "historique_call_to_action": "Consulter la synthèse",
                            "historique_type": "réponse et pourcentage",
                            "createdAt": "2024-06-21T14:40:01.183Z",
                            "updatedAt": "2024-07-02T10:01:05.469Z",
                            "publishedAt": "2024-06-21T14:40:02.833Z",
                            "datetime_publication": "2024-06-30T23:00:00.000Z"
                          }
                        }
                      ]
                    },
                    "consultation_contenu_a_venir": {
                      "data": {
                        "id": 1,
                        "attributes": {
                          "titre_historique": "Mise en oeuvre",
                          "createdAt": "2024-06-21T14:40:33.341Z",
                          "updatedAt": "2024-06-21T14:40:34.695Z",
                          "publishedAt": "2024-06-21T14:40:34.690Z"
                        }
                      }
                    }
                  }
                }
              ],
              "meta": {
                "pagination": {
                  "page": 1,
                  "pageSize": 25,
                  "pageCount": 1,
                  "total": 1
                }
              }
            }
        """.trimIndent()

        // WHEN
        val ref = object : TypeReference<StrapiDTO<ConsultationStrapiDTO>>() {}
        val actual = objectMapper.readValue(jsonConsultation, ref)

        // THEN
        assertThat(actual.data[0].attributes.titre).isEqualTo("Mieux rémunérer les agriculteurs : quels leviers pour le consommateur ?")
    }
}
