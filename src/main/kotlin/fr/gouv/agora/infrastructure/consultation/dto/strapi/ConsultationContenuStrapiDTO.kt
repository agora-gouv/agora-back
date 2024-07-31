package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class StrapiConsultationContenuAvantReponse(
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class StrapiConsultationContenuApresReponse(
    @JsonProperty("template_partage_avant_fin_consultation")
    val templatePartageAvantFinConsultation: String,
    @JsonProperty("template_partage_apres_fin_consultation")
    val templatePartageApresFinConsultation: String,
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
