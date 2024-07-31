package fr.gouv.agora.infrastructure.consultation.dto

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class ConsultationStrapiDTOTest {
    private val objectMapper = jacksonObjectMapper()
        .registerKotlinModule()
        .registerModules(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)

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
                    "updatedAt": "2024-07-05T12:54:26.863Z",
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
                            "text": "Les États généraux de l'alimentation (EGAlim), réunis en 2017, répondaient à un engagement fort du Président de la République en faveur d’une alimentation saine et durable et de relations commerciales plus équilibrées entre la grande distribution, les industriels et les agriculteurs. Ils ont offert un temps de réflexion partagée pour construire des solutions nouvelles.",
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
                            "text": "...",
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
                            "text": "🌵 Cactus !",
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
                        "popup_explication": null,
                        "choix": [
                          {
                            "id": 2,
                            "label": "Le rapport quantité/prix",
                            "ouvert": false
                          },
                          {
                            "id": 3,
                            "label": "La rapport qualité/prix",
                            "ouvert": false
                          },
                          {
                            "id": 4,
                            "label": "L’origine",
                            "ouvert": false
                          },
                          {
                            "id": 5,
                            "label": "L'aspect visuel",
                            "ouvert": false
                          },
                          {
                            "id": 6,
                            "label": "Les promotions",
                            "ouvert": false
                          },
                          {
                            "id": 7,
                            "label": "La marque",
                            "ouvert": false
                          },
                          {
                            "id": 8,
                            "label": "Les signes de qualité (labels, certifications, etc.)",
                            "ouvert": false
                          },
                          {
                            "id": 9,
                            "label": "Les conséquences sur l’environnement",
                            "ouvert": false
                          },
                          {
                            "id": 10,
                            "label": "L’apport nutritionnel",
                            "ouvert": false
                          },
                          {
                            "id": 11,
                            "label": "Autre (précisez)",
                            "ouvert": true
                          }
                        ]
                      },
                      {
                        "id": 3,
                        "__component": "question-de-consultation.question-a-choix-unique",
                        "titre": "Quelle est la première raison de votre choix de lieu de courses ?",
                        "numero": 2,
                        "popup_explication": null,
                        "choix": [
                          {
                            "id": 12,
                            "label": "La proximité (de mon domicile ou de mon travail)",
                            "ouvert": false
                          },
                          {
                            "id": 14,
                            "label": "Le prix",
                            "ouvert": false
                          },
                          {
                            "id": 13,
                            "label": "La diversité de l’offre",
                            "ouvert": false
                          },
                          {
                            "id": 15,
                            "label": "La qualité de l’offre",
                            "ouvert": false
                          },
                          {
                            "id": 16,
                            "label": "*Autre : précisez votre réponse",
                            "ouvert": true
                          }
                        ]
                      },
                      {
                        "id": 4,
                        "__component": "question-de-consultation.question-a-choix-unique",
                        "titre": "La présence d’un label (Label rouge, Bio, AOP) sur un produit alimentaire vous rassure-t-elle quant à la rémunération de l’agriculteur ?",
                        "numero": 3,
                        "popup_explication": null,
                        "choix": [
                          {
                            "id": 17,
                            "label": "Oui, tout à fait",
                            "ouvert": false
                          },
                          {
                            "id": 18,
                            "label": "Oui, plutôt",
                            "ouvert": false
                          },
                          {
                            "id": 19,
                            "label": "Non, pas vraiment",
                            "ouvert": false
                          },
                          {
                            "id": 20,
                            "label": "Non, pas du tout",
                            "ouvert": false
                          }
                        ]
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
                        "popup_explication": null,
                        "choix": [
                          {
                            "id": 21,
                            "label": "Augmenter la part qui doit revenir à l’agriculteur dans le prix de vente",
                            "ouvert": false
                          },
                          {
                            "id": 23,
                            "label": "Améliorer la transparence sur la répartition de la valeur entre l’agriculteur, l’industriel et la grande distribution",
                            "ouvert": false
                          },
                          {
                            "id": 22,
                            "label": "Renforcer la visibilité de l’origine des produits",
                            "ouvert": false
                          },
                          {
                            "id": 25,
                            "label": "Améliorer l’information sur la qualité des produits",
                            "ouvert": false
                          },
                          {
                            "id": 24,
                            "label": "Améliorer l’information sur l’impact environnemental des produits",
                            "ouvert": false
                          },
                          {
                            "id": 26,
                            "label": "*Autre : précisez votre réponse",
                            "ouvert": true
                          },
                          {
                            "id": 27,
                            "label": "Je ne suis pas prêt(e) à payer plus cher",
                            "ouvert": false
                          },
                          {
                            "id": 28,
                            "label": "Je ne peux pas payer plus cher",
                            "ouvert": false
                          }
                        ]
                      },
                      {
                        "id": 4,
                        "__component": "question-de-consultation.question-a-choix-multiples",
                        "titre": "Quels produits seriez-vous prêt(e) à payer un peu plus cher pour mieux rémunérer les agriculteurs ?",
                        "numero": 6,
                        "nombre_maximum_de_choix": 3,
                        "popup_explication": null,
                        "choix": [
                          {
                            "id": 32,
                            "label": "Les fruits et légumes",
                            "ouvert": false
                          },
                          {
                            "id": 29,
                            "label": "Les produits laitiers (laits, fromages, yaourts, etc.)",
                            "ouvert": false
                          },
                          {
                            "id": 30,
                            "label": "Les œufs ",
                            "ouvert": false
                          },
                          {
                            "id": 31,
                            "label": "Les viandes",
                            "ouvert": false
                          },
                          {
                            "id": 33,
                            "label": "Les plats préparés",
                            "ouvert": false
                          },
                          {
                            "id": 34,
                            "label": "Les produits d’épicerie sèche (pâtes, riz, pois-chiches, etc.)",
                            "ouvert": false
                          },
                          {
                            "id": 35,
                            "label": "Les produits transformés sucrés (gâteaux, pâtes à tartiner, bonbons, etc.)",
                            "ouvert": false
                          },
                          {
                            "id": 36,
                            "label": "Les produits de boulangerie (pain, viennoiserie, etc.)",
                            "ouvert": false
                          },
                          {
                            "id": 37,
                            "label": "*Autre : précisez votre réponse",
                            "ouvert": true
                          },
                          {
                            "id": 38,
                            "label": "Tous les produits",
                            "ouvert": false
                          },
                          {
                            "id": 39,
                            "label": "Je ne suis pas prêt(e) à payer plus cher",
                            "ouvert": false
                          },
                          {
                            "id": 40,
                            "label": "Je ne peux pas payer plus cher ",
                            "ouvert": false
                          }
                        ]
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
                        "popup_explication": null,
                        "choix": [
                          {
                            "id": 41,
                            "label": "Actualiser plus régulièrement les coûts de production des agriculteurs pour permettre de payer un prix juste",
                            "ouvert": false
                          },
                          {
                            "id": 42,
                            "label": "Donner plus de place aux agriculteurs dans la construction des prix des produits alimentaires",
                            "ouvert": false
                          },
                          {
                            "id": 45,
                            "label": "Encourager l’association des agriculteurs au sein de coopératives ou d’organisations de producteurs pour équilibrer le rapport de force avec les industriels et les commerçants dans la négociation des prix",
                            "ouvert": false
                          },
                          {
                            "id": 43,
                            "label": "Développer la vente directe ou les circuits courts",
                            "ouvert": false
                          },
                          {
                            "id": 44,
                            "label": "Définir un seuil en deçà duquel un industriel ou un distributeur ne peut pas acheter un produit alimentaire",
                            "ouvert": false
                          },
                          {
                            "id": 46,
                            "label": "Verser un complément de salaires aux agriculteurs compte tenu du fait que l’agriculture est considérée comme un enjeu de souveraineté nationale",
                            "ouvert": false
                          },
                          {
                            "id": 50,
                            "label": "Imposer aux produits importés hors de l’Union européenne de respecter les mêmes règles sociales et environnementales que les agriculteurs français et européens",
                            "ouvert": false
                          },
                          {
                            "id": 47,
                            "label": "Prévoir des règles plus simples et moins d’encadrement dans les contrats de vente avec les industriels et les commerçants",
                            "ouvert": false
                          },
                          {
                            "id": 48,
                            "label": "*Autre : précisez votre réponse ",
                            "ouvert": true
                          },
                          {
                            "id": 49,
                            "label": "Je ne sais pas",
                            "ouvert": false
                          }
                        ]
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
                          "createdAt": "2024-06-21T12:59:00.544Z",
                          "updatedAt": "2024-07-02T10:00:56.724Z",
                          "publishedAt": "2024-06-21T12:59:01.521Z",
                          "sections": [
                            {
                              "id": 1,
                              "__component": "consultation-section.section-titre",
                              "titre": "Pourquoi cette consultation ?"
                            },
                            {
                              "id": 1,
                              "__component": "consultation-section.section-texte-riche",
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
                              ]
                            }
                          ]
                        }
                      }
                    },
                    "consultation_apres_reponse_ou_terminee": {
                      "data": {
                        "id": 1,
                        "attributes": {
                          "template_partage_avant_fin_consultation": "Comme moi, tu peux participer à la consultation : {title} {url}",
                          "template_partage_apres_fin_consultation": "Les premiers résultats de la consultation {title} : {url}",
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
                          "createdAt": "2024-06-21T14:23:51.194Z",
                          "updatedAt": "2024-07-09T13:15:13.923Z",
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
                          "sections": [
                            {
                              "id": 2,
                              "__component": "consultation-section.section-texte-riche",
                              "description": [
                                {
                                  "type": "paragraph",
                                  "children": [
                                    {
                                      "text": "👉 A partir des résultats de cette consultation, la mission parlementaire reviendra vers vous pour présenter son rapport et les contributions qu'elle a retenues.",
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
                                      "text": "Le Gouvernement retiendra, ensuite, les propositions de réforme des lois EGAlim qui lui semblent nécessaires pour garantir une rémunération plus juste des agriculteurs.",
                                      "type": "text"
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "id": 2,
                              "__component": "consultation-section.section-chiffre",
                              "titre": "chiffre",
                              "description": [
                                {
                                  "type": "paragraph",
                                  "children": [
                                    {
                                      "text": "CINQUANTE",
                                      "type": "text"
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "id": 1,
                              "__component": "consultation-section.section-citation",
                              "description": [
                                {
                                  "type": "paragraph",
                                  "children": [
                                    {
                                      "text": "La pomme est plus grande que l'arbre",
                                      "type": "text"
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "id": 1,
                              "__component": "consultation-section.section-image",
                              "url": "https://content.agora.incubateur.net/portraits/QaG-OlivierVeran.png",
                              "description_accessible_de_l_image": "Olivier Veran"
                            },
                            {
                              "id": 5,
                              "__component": "consultation-section.section-texte-riche",
                              "description": [
                                {
                                  "type": "paragraph",
                                  "children": [
                                    {
                                      "text": "texte ",
                                      "type": "text"
                                    },
                                    {
                                      "bold": true,
                                      "text": "riche",
                                      "type": "text"
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "id": 3,
                              "__component": "consultation-section.section-titre",
                              "titre": "titre stylé"
                            },
                            {
                              "id": 1,
                              "__component": "consultation-section.section-video",
                              "url": "https://github.com/agora-gouv/agora-content/blob/main/consultation_videos/2024-06-12-handicap.mp4",
                              "largeur": 500,
                              "hauteur": 1000,
                              "nom_auteur": "Auteur vidéo",
                              "poste_auteur": "Ministre",
                              "date_tournage": "2024-07-02",
                              "transcription": "Oui bonjour"
                            }
                          ]
                        }
                      }
                    },
                    "consultation_contenu_autres": {
                      "data": [
                          {
                            "id": 18003,
                            "attributes": {
                              "message_mise_a_jour": "Analyse disponible",
                              "lien_telechargement_analyse": "https://content.agora.incubateur.net/consultation-syntheses/20240605-agora_handicap_synthese_VF.pdf",
                              "template_partage": "Cela peut t'intéresser : l'analyse des réponses de la consultation {title} {url}",
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
                              "createdAt": "2024-06-21T14:40:01.183Z",
                              "updatedAt": "2024-07-24T09:49:23.682Z",
                              "publishedAt": "2024-07-24T09:49:12.421Z",
                              "datetime_publication": "2024-06-30T23:00:00.000Z",
                              "sections": [
                                {
                                  "id": 6,
                                  "__component": "consultation-section.section-titre",
                                  "titre": "Analyse des réponses"
                                },
                                {
                                  "id": 11,
                                  "__component": "consultation-section.section-texte-riche",
                                  "description": [
                                    {
                                      "type": "paragraph",
                                      "children": [
                                        {
                                          "bold": true,
                                          "text": "56% des participants",
                                          "type": "text"
                                        },
                                        {
                                          "text": " estiment être suffisamment sensibilisés à la question du handicap, mais leur perception est radicalement différente en ce qui concerne le niveau de sensibilisation de la société française.",
                                          "type": "text"
                                        }
                                      ]
                                    }
                                  ]
                                },
                                {
                                  "id": 5,
                                  "__component": "consultation-section.section-chiffre",
                                  "titre": "61%",
                                  "description": [
                                    {
                                      "type": "paragraph",
                                      "children": [
                                        {
                                          "text": "des participants pensent que la ",
                                          "type": "text"
                                        },
                                        {
                                          "bold": true,
                                          "text": "société française",
                                          "type": "text"
                                        },
                                        {
                                          "text": " n’est pas suffisamment sensibilisée au handicap.",
                                          "type": "text"
                                        }
                                      ]
                                    }
                                  ]
                                },
                                {
                                  "id": 10,
                                  "__component": "consultation-section.section-texte-riche",
                                  "description": [
                                    {
                                      "type": "paragraph",
                                      "children": [
                                        {
                                          "bold": true,
                                          "text": "99% des participants",
                                          "type": "text"
                                        },
                                        {
                                          "text": " estiment être suffisamment sensibilisés à la question du handicap, mais leur perception est radicalement différente en ce qui concerne le niveau de sensibilisation de la société française.",
                                          "type": "text"
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            }
                          },
                          {
                            "id": 18002,
                            "attributes": {
                              "message_mise_a_jour": "Mise à jour",
                              "lien_telechargement_analyse": null,
                              "template_partage": "Cela peut t'intéresser : l'analyse des réponses de la consultation {title} {url}",
                              "feedback_pictogramme": "💬",
                              "feedback_titre": "Donnez votre avis",
                              "feedback_description": [
                                {
                                  "type": "paragraph",
                                  "children": [
                                    {
                                      "text": "Êtes-vous satisfait(e) de l'analyse de cette consultation  ????",
                                      "type": "text"
                                    }
                                  ]
                                }
                              ],
                              "historique_titre": "Test de mise à jour",
                              "historique_call_to_action": "Test de mise à jour",
                              "createdAt": "2024-06-21T14:40:01.183Z",
                              "updatedAt": "2024-07-24T09:48:33.635Z",
                              "publishedAt": "2024-07-24T09:48:33.628Z",
                              "datetime_publication": "2024-06-30T23:00:00.000Z",
                              "sections": [
                                {
                                  "id": 7,
                                  "__component": "consultation-section.section-texte-riche",
                                  "description": [
                                    {
                                      "type": "paragraph",
                                      "children": [
                                        {
                                          "bold": true,
                                          "text": "Test de mise à jour",
                                          "type": "text"
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
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
        assertThat(actual.data[0].attributes.consultationContenuAutres.data).hasSize(2)
        assertThat(actual.data[0].attributes.consultationContenuAVenir).isNotNull
        assertThat(actual.data[0].attributes.contenuAvantReponse.data.attributes.sections).hasSize(2)
        assertThat(actual.data[0].attributes.contenuApresReponseOuTerminee.data.attributes.sections).hasSize(7)
    }
}
