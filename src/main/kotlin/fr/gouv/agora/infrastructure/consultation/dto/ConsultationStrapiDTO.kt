package fr.gouv.agora.infrastructure.consultation.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiDataList
import fr.gouv.agora.infrastructure.common.StrapiRichText
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import java.time.LocalDate
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class ConsultationStrapiDTO(
    @JsonProperty(value = "titre_consultation")
    val titre: String,
    @JsonProperty(value = "datetime_de_debut")
    val dateDeDebut: LocalDateTime,
    @JsonProperty(value = "datetime_de_fin")
    val dateDeFin: LocalDateTime,
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
    val nombreParticipantsCible: Int,
    @JsonProperty(value = "flamme_label")
    val flammeLabel: String,
    @JsonProperty(value = "description")
    val description: List<StrapiRichText>,
    @JsonProperty(value = "objectifs")
    val objectifs: List<StrapiRichText>,
    @JsonProperty(value = "thematique")
    val thematique: StrapiData<StrapiThematiqueDTO>,
    @JsonProperty(value = "questions")
    val questions: List<StrapiConsultationQuestion>,
    @JsonProperty(value = "consultation_avant_reponse")
    val contenuAvantReponse: StrapiData<StrapiConsultationContenuAvantReponse>,
    @JsonProperty(value = "consultation_apres_reponse_ou_terminee")
    val contenuApresReponseOuTerminee: StrapiData<StrapiConsultationContenuApresReponse>,
    @JsonProperty("consultation_contenu_autres")
    val consultationContenuAutres: StrapiDataList<StrapiConsultationContenuAutre>,
    @JsonProperty("consultation_contenu_a_venir")
    val consultationContenuAVenir: StrapiData<StrapiConsultationAVenir>?,
) {
    fun getLatestUpdateDate(now: LocalDateTime): LocalDateTime? {
        return listOfNotNull(
            *consultationContenuAutres.data.map { it.attributes.datetimePublication }.toTypedArray(),
            contenuApresReponseOuTerminee.data.attributes.datetimePublication,
            contenuAvantReponse.data.attributes.datetimePublication,
        ).filter { it.isBefore(now) }.maxOrNull()
    }
}

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
@JsonIgnoreProperties("id", "__component", "createdAt", "updatedAt", "publishedAt")
sealed interface StrapiConsultationQuestion

data class StrapiConsultationQuestionChoixMultiples(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    val numero: String,
    @JsonProperty("nombre_maximum_de_choix")
    val nombreMaximumDeChoix: String,
    @JsonProperty("choix")
    val choix: List<StrapiConsultationChoix>,
    @JsonProperty("popup_explication")
    val popupExplication: String?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionChoixUnique(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    val numero: String,
    @JsonProperty("choix")
    val choix: List<StrapiConsultationChoix>,
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

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class StrapiConsultationContenuAvantReponse(
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("historique_type")
    val historiqueType: String,
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "__component",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = StrapiConsultationSectionTitre::class,
        name = "consultation-section.section-titre"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionRichText::class,
        name = "consultation-section.section-texte-riche"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionCitation::class,
        name = "consultation-section.section-citation"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionImage::class,
        name = "consultation-section.section-image"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionVideo::class,
        name = "consultation-section.section-video"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionChiffre::class,
        name = "consultation-section.section-chiffre"
    ),
)
@JsonIgnoreProperties("__component", "createdAt", "updatedAt", "publishedAt")
sealed interface StrapiConsultationSection

data class StrapiConsultationSectionCitation(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationSection

data class StrapiConsultationSectionImage(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("description_accessible_de_l_image")
    val descriptionImage: String,
) : StrapiConsultationSection

data class StrapiConsultationSectionRichText(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationSection

data class StrapiConsultationSectionTitre(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("titre")
    val titre: String,
) : StrapiConsultationSection

data class StrapiConsultationSectionChiffre(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationSection

data class StrapiConsultationSectionVideo(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("largeur")
    val largeur: Int,
    @JsonProperty("hauteur")
    val hauteur: Int,
    @JsonProperty("nom_auteur")
    val nomAuteur: String,
    @JsonProperty("poste_auteur")
    val posteAuteur: String,
    @JsonProperty("date_tournage")
    val dateTournage: LocalDate,
    @JsonProperty("transcription")
    val transcription: String,
) : StrapiConsultationSection

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
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
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class StrapiConsultationContenuAutre(
    @JsonProperty("message_mise_a_jour")
    val messageMiseAJour: String,
    @JsonProperty("lien_telechargement_analyse")
    val lienTelechargementAnalyse: String?,
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
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("historique_type")
    val historiqueType: String,
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class StrapiConsultationAVenir(
    @JsonProperty("titre_historique")
    val titreHistorique: String,
)

data class StrapiConsultationChoix(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ouvert")
    val ouvert: Boolean,
)
