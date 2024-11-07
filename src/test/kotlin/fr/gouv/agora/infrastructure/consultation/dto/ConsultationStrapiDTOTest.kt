package fr.gouv.agora.infrastructure.consultation.dto

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSection
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
              "data": [{
                "id": 1,
                "attributes": {
                  "url_image_de_couverture": "https://content.agora.beta.gouv.fr/consultation_covers/agriculteurs_professionnels.jpg",
                  "url_image_page_de_contenu": "https://content.agora.beta.gouv.fr/consultation_covers/agriculteurs_consommateurs.jpg",
                  "nombre_de_questions": 3,
                  "estimation_nombre_de_questions": "2 à 3 questions",
                  "estimation_temps": "8 minutes",
                  "nombre_participants_cible": 12000,
                  "createdAt": "2024-05-29T09:37:12.407Z",
                  "updatedAt": "2024-08-23T13:04:14.174Z",
                  "publishedAt": "2024-07-09T12:25:24.599Z",
                  "datetime_de_debut": "2024-07-01T22:00:00.000Z",
                  "datetime_de_fin": "2024-07-10T22:00:00.000Z",
                  "titre_consultation": "Agriculture professionnel",
                  "slug": "agriculture-professionnel",
                  "territoire": "Nord",
                  "thematique": {
                    "data": {
                      "id": 9,
                      "attributes": {
                        "label": "Démocratie",
                        "pictogramme": "🗳",
                        "createdAt": "2024-05-29T10:19:13.352Z",
                        "updatedAt": "2024-05-29T10:19:13.352Z",
                        "publishedAt": "2024-05-29T10:19:13.352Z"
                      }
                    }
                  },
                  "questions": [
                    {
                      "id": 1,
                      "__component": "question-de-consultation.question-a-choix-unique",
                      "titre": "Qu'en pensez-vous ?",
                      "numero": 1,
                      "popup_explication": null,
                      "question_suivante": null,
                      "choix": [
                        {
                          "id": 2,
                          "label": "J'adore !",
                          "ouvert": true
                        }
                      ]
                    }
                  ],
                  "consultation_avant_reponse": {
                    "data": {
                      "id": 6003,
                      "attributes": {
                        "template_partage": "Comme moi, tu peux participer à la Consultation : {title} {url}",
                        "historique_titre": "Lancement",
                        "historique_call_to_action": "Voir les objectifs",
                        "createdAt": "2024-08-23T12:10:24.430Z",
                        "updatedAt": "2024-08-23T13:01:55.776Z",
                        "slug": "lancement",
                        "commanditaire": [
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "Gouvernement",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "objectif": [
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "sdsdsd",
                                "type": "text",
                                "italic": true
                              },
                              {
                                "bold": true,
                                "text": "qdsdsds",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "axe_gouvernemental": [
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "Culture",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "presentation": [
                          {
                            "type": "paragraph",
                            "children": [
                              {
                                "text": "Pouet",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "nom_strapi": "Agriculture pro - objectifs",
                        "sections": [
                          {
                            "id": 1,
                            "__component": "consultation-section.section-titre",
                            "titre": "Titre"
                          }
                        ]
                      }
                    }
                  },
                  "consultation_apres_reponse_ou_terminee": {
                    "data": {
                      "id": 2,
                      "attributes": {
                        "historique_titre": "Fin de consultation",
                        "historique_call_to_action": "Consulter toutes les réponses",
                        "createdAt": "2024-08-23T13:01:07.218Z",
                        "updatedAt": "2024-08-23T13:01:44.327Z",
                        "slug": "fin-de-la-consultation",
                        "feedback_message": "Êtes-vous satisfait(e) de cette consultation ?",
                        "nom_strapi": "Agriculture pro - plus loin",
                        "template_partage": "Comme moi, tu peux participer à la Consultation : {title} {url}",
                        "sections": [
                          {
                            "id": 1,
                            "__component": "consultation-section.section-chiffre",
                            "titre": "12%",
                            "description": [
                              {
                                "type": "paragraph",
                                "children": [
                                  {
                                    "text": "Bienvenue",
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
                  "consultation_contenu_autres": {
                    "data": [
                      {
                        "id": 2,
                        "attributes": {
                          "template_partage": "Cela peut t’intéresser : l’analyse des réponses de la consultation {title} {url}",
                          "historique_titre": "Nouveau contenu autre",
                          "historique_call_to_action": "cliquez ici",
                          "createdAt": "2024-08-23T13:03:40.424Z",
                          "updatedAt": "2024-08-23T13:03:49.338Z",
                          "datetime_publication": "2024-08-22T15:00:00.000Z",
                          "slug": "cliquez-ici",
                          "feedback_message": "Êtes-vous satisfait(e) de cette consultation ?",
                          "nom_strapi": "Agriculture pro - autre",
                          "flamme_label": "Trop bien",
                          "sections": [
                            {
                              "id": 1,
                              "__component": "consultation-section.section-accordeon",
                              "titre": "Accord",
                              "description": [
                                {
                                  "type": "paragraph",
                                  "children": [
                                    {
                                      "text": "éon",
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
                      "id": 3,
                      "attributes": {
                        "titre_historique": "Ca arrive",
                        "createdAt": "2024-08-23T13:03:57.597Z",
                        "updatedAt": "2024-08-23T13:03:57.597Z"
                      }
                    }
                  },
                  "consultation_contenu_analyse_des_reponse": {
                    "data": {
                      "id": 3,
                      "attributes": {
                        "lien_telechargement_analyse": "https://content.agora.incubateur.net/consultation-syntheses/20240605-agora_handicap_synthese_VF.pdf",
                        "createdAt": "2024-08-23T13:01:25.877Z",
                        "updatedAt": "2024-08-23T13:01:33.261Z",
                        "template_partage": "Cela peut t’intéresser : l’analyse des réponses de la consultation {title} {url}",
                        "datetime_publication": "2024-08-21T22:00:00.000Z",
                        "slug": "analyse-disponible",
                        "feedback_message": "Êtes-vous satisfait(e) de l'analyse de cette consultation ?",
                        "historique_titre": "Analyse des réponses",
                        "historique_call_to_action": "Consulter la synthèse",
                        "nom_strapi": "Agriculture pro - analyse",
                        "flamme_label": "Venez répondre !!",
                        "sections": [
                          {
                            "id": 1,
                            "__component": "consultation-section.section-citation",
                            "description": [
                              {
                                "type": "paragraph",
                                "children": [
                                  {
                                    "text": "Oui",
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
                  "contenu_reponse_du_commanditaires": {
                    "data": {
                      "id": 2,
                      "attributes": {
                        "template_partage": "Cela peut t’intéresser : la réponse du gouvernement sur la consultation {title} {url}",
                        "datetime_publication": "2024-08-21T23:00:00.000Z",
                        "slug": "reponse-du-gouvernement",
                        "feedback_message": "Êtes-vous satisfait(e) de la réponse de la ministre ?",
                        "createdAt": "2024-08-23T13:03:01.978Z",
                        "updatedAt": "2024-08-23T13:03:07.029Z",
                        "historique_titre": "Réponse du Gouvernement",
                        "historique_call_to_action": "Actions mises en place",
                        "nom_strapi": "Agriculture pro - réponse du gouv",
                        "flamme_label": "Ils ont répondu !!",
                        "sections": []
                      }
                    }
                  }
                }
              }],
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
        assertThat(actual.data[0].attributes.titre).isEqualTo("Agriculture professionnel")
        assertThat(actual.data[0].attributes.consultationContenuAutres.data).hasSize(1)
        assertThat(actual.data[0].attributes.consultationContenuAVenir).isNotNull
    }

    @Test
    fun `parse sections`() {
        // GIVEN
        @Language("JSON")
        val jsonSections ="""
            [
              {
                "id": 2,
                "__component": "consultation-section.section-chiffre",
                "titre": "Chiffre",
                "description": [
                  {
                    "type": "paragraph",
                    "children": [
                      {
                        "text": "Description chiffre",
                        "type": "text"
                      }
                    ]
                  }
                ]
              },
              {
                "id": 2,
                "__component": "consultation-section.section-citation",
                "description": [
                  {
                    "type": "paragraph",
                    "children": [
                      {
                        "text": "Description citation",
                        "type": "text"
                      }
                    ]
                  }
                ]
              },
              {
                "id": 1,
                "__component": "consultation-section.section-image",
                "url": "url image",
                "description_accessible_de_l_image": "Description image"
              },
              {
                "id": 1,
                "__component": "consultation-section.section-texte-riche",
                "description": [
                  {
                    "type": "paragraph",
                    "children": [
                      {
                        "text": "Description texte riche",
                        "type": "text"
                      }
                    ]
                  }
                ]
              },
              {
                "id": 2,
                "__component": "consultation-section.section-titre",
                "titre": "titre"
              },
              {
                "id": 1,
                "__component": "consultation-section.section-video",
                "url": "url vidéo",
                "largeur": 200,
                "hauteur": 300,
                "nom_auteur": "nom",
                "poste_auteur": "poste",
                "date_tournage": "2024-08-26",
                "transcription": "transcription vidéo"
              },
              {
                "id": 1,
                "__component": "consultation-section.section-accordeon",
                "titre": "Accordéon",
                "description": [
                  {
                    "type": "paragraph",
                    "children": [
                      {
                        "text": "Description accordéon",
                        "type": "text"
                      }
                    ]
                  }
                ]
              }
            ]
        """.trimIndent()

        // WHEN
        val ref = object : TypeReference<List<StrapiConsultationSection>>() {}
        val actual = objectMapper.readValue(jsonSections, ref)

        // THEN
        assertThat(actual).hasSize(7)
    }
}
