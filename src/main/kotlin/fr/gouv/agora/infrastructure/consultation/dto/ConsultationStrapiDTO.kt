package fr.gouv.agora.infrastructure.consultation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTOX
import fr.gouv.agora.infrastructure.common.StrapiDTOXX
import fr.gouv.agora.infrastructure.common.StrapiRichText
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import java.time.LocalDate
import java.time.LocalDateTime

data class ConsultationStrapiDTO(
    @JsonProperty(value = "titre")
    val titre: String,
    @JsonProperty(value = "date_de_debut")
    val dateDeDebut: LocalDate,
    @JsonProperty(value = "date_de_fin")
    val dateDeFin: LocalDate,
    @JsonProperty(value = "url_image_de_couverture")
    val urlImageDeCouverture: String,
    @JsonProperty(value = "url_image_page_de_contenu")
    val urlImagePageDeContenu: String,
    @JsonProperty(value = "nombre_de_questions")
    val nombreDeQuestion: Int,
    @JsonProperty(value = "estimation_nombre_de_questions")
    val estimationNombreDeQuestions: String,
    @JsonProperty(value = "estimation_temps")
    val estimationTemps: String,
    @JsonProperty(value = "nombre_participants_cible")
    val nombreParticipantsCible: String,
    @JsonProperty(value = "createdAt")
    val createdAt: LocalDateTime,
    @JsonProperty(value = "updatedAt")
    val updatedAt: LocalDateTime,
    @JsonProperty(value = "publishedAt")
    val publishedAt: LocalDateTime,
    @JsonProperty(value = "thematique")
    val thematique: StrapiDTOXX<StrapiThematiqueDTO>,
    @JsonProperty(value = "questions")
    val questions: List<StrapiConsultationQuestion>,
    @JsonProperty(value = "consultation_avant_reponse")
    val contenuAvantReponse: StrapiDTOXX<StrapiConsultationContenuAvantReponse>?,
    @JsonProperty(value = "consultation_apres_reponse_ou_terminee")
    val contenuApresReponseOuTerminee: StrapiDTOXX<StrapiConsultationContenuApresReponse>?,
    @JsonProperty("consultation_contenu_autres")
    val consultationContenuAutres : StrapiDTOX<StrapiConsultationContenuAutre>,
    @JsonProperty("consultation_contenu_a_venir")
    val consultationContenuAVenir : StrapiDTOXX<StrapiConsultationAVenir>?,
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "__component",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = StrapiConsultationQuestionChoixMultiples::class,
        name = "question-de-consultation.question-a-choix-multiples"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationQuestionChoixUnique::class,
        name = "question-de-consultation.question-a-choix-unique"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationQuestionOuverte::class,
        name = "question-de-consultation.question-ouverte"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationQuestionDescription::class,
        name = "question-de-consultation.description"
    ),
)
sealed interface StrapiConsultationQuestion

data class StrapiConsultationQuestionChoixMultiples(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    val numero: String,
    @JsonProperty("nombre_maximum_de_choix")
    val nombreMaximumDeChoix: String,
    @JsonProperty("popup_explication")
    val popupExplication: String?,
    // todo : ajouter les choix
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionChoixUnique(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    val numero: String,
    @JsonProperty("popup_explication")
    val popupExplication: String?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionOuverte(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    val numero: String,
    @JsonProperty("popup_explication")
    val popupExplication: String?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionDescription(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    val numero: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationQuestion

data class StrapiConsultationContenuAvantReponse(
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("historique_type")
    val historiqueType: String,
    @JsonProperty("liste_objectifs")
    val listeObjectifs: List<StrapiRichText>,
    @JsonProperty("createdAt")
    val createdAt: String,
    @JsonProperty("updatedAt")
    val updatedAt: String,
    @JsonProperty("publishedAt")
    val publishedAt: String,
)

data class StrapiConsultationContenuApresReponse(
    @JsonProperty("template_partage_avant_fin_consultation")
    val templatePartageAvantFinConsultation: String,
    @JsonProperty("template_partage_apres_fin_consultation")
    val templatePartageApresFinConsultation: String,
    @JsonProperty("footer_titre")
    val footerTitre: String,
    @JsonProperty("footer_description")
    val footerDescription: List<StrapiRichText>,
    @JsonProperty("feedback_pictogramme")
    val feedbackPictogramme: String,
    @JsonProperty("feedback_titre")
    val feedbackTitre: String,
    @JsonProperty("feedback_description")
    val feedbackDescription: List<StrapiRichText>,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("historique_type")
    val historiqueType: String,
    @JsonProperty("createdAt")
    val createdAt: LocalDateTime,
    @JsonProperty("updatedAt")
    val updatedAt: LocalDateTime,
    @JsonProperty("publishedAt")
    val publishedAt: LocalDateTime,
    @JsonProperty("encart_visualisation_resultat_avant_fin_consultation_picto")
    val encartVisualisationResultatAvantFinConsultationPictogramme: String,
    @JsonProperty("encart_visualisation_resultat_avant_fin_consultation_desc")
    val encartVisualisationResultatAvantFinConsultationDescription: List<StrapiRichText>,
    @JsonProperty("encart_visualisation_resultat_avant_fin_consultation_cta")
    val encartVisualisationResultatAvantFinConsultationCallToAction: String,
    @JsonProperty("encart_visualisation_resultat_apres_fin_consultation_picto")
    val encartVisualisationResultatApresFinConsultationPictogramme: String,
    @JsonProperty("encart_visualisation_resultat_apres_fin_consultation_desc")
    val encartVisualisationResultatApresFinConsultationDescription: List<StrapiRichText>,
    @JsonProperty("encart_visualisation_resultat_apres_fin_consultation_cta")
    val encartVisualisationResultatApresFinConsultationCallToAction: String,
)

data class StrapiConsultationContenuAutre(
    @JsonProperty("message_mise_a_jour")
    val messageMiseAJour: String,
    @JsonProperty("lien_telechargement_analyse")
    val lienTelechargementAnalyse: String,
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("header_titre")
    val headerTitre: String,
    @JsonProperty("header_description")
    val headerDescription: List<StrapiRichText>,
    @JsonProperty("footer_titre")
    val footerTitre: String,
    @JsonProperty("footer_description")
    val footerDescription: List<StrapiRichText>,
    @JsonProperty("feedback_pictogramme")
    val feedbackPictogramme: String,
    @JsonProperty("feedback_titre")
    val feedbackTitre: String,
    @JsonProperty("feedback_description")
    val feedbackDescription: List<StrapiRichText>,
    @JsonProperty("date_publication")
    val datePublication: LocalDate,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("historique_type")
    val historiqueType: String,
    @JsonProperty("createdAt")
    val createdAt: LocalDateTime,
    @JsonProperty("updatedAt")
    val updatedAt: LocalDateTime,
    @JsonProperty("publishedAt")
    val publishedAt: LocalDateTime,
)

data class StrapiConsultationAVenir(
    @JsonProperty("titre_historique")
    val titreHistorique: String,
    @JsonProperty("createdAt")
    val createdAt: LocalDateTime,
    @JsonProperty("updatedAt")
    val updatedAt: LocalDateTime,
    @JsonProperty("publishedAt")
    val publishedAt: LocalDateTime,
)
