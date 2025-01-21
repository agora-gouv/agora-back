package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "nom_strapi")
data class StrapiConsultationContenuAvantReponse(
    @JsonProperty("slug")
    val slug: String,
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("commanditaire")
    val commanditaire: List<StrapiRichText>,
    @JsonProperty("objectif")
    val objectif: List<StrapiRichText>,
    @JsonProperty("axe_gouvernemental")
    val axeGouvernemental: List<StrapiRichText>,
    @JsonProperty("presentation")
    val presentation: List<StrapiRichText>,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "nom_strapi")
data class StrapiConsultationContenuApresReponse(
    @JsonProperty("slug")
    val slug: String,
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("feedback_message")
    val feedbackMessage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "nom_strapi")
data class StrapiConsultationContenuAutre(
    @JsonProperty("slug")
    val slug: String,
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("feedback_message")
    val feedbackMessage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
    @JsonProperty("flamme_label")
    val flammeLabel: String,
    @JsonProperty("recap_emoji")
    val recapEmoji: String,
    @JsonProperty("recap_label")
    val recapLabel: String,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class StrapiConsultationAVenir(
    @JsonProperty("titre_historique")
    val titreHistorique: String,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "nom_strapi")
data class StrapiConsultationAnalyseDesReponses(
    @JsonProperty("lien_telechargement_analyse")
    val lienTelechargementAnalyse: String,
    @JsonProperty("slug")
    val slug: String,
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
    @JsonProperty("feedback_message")
    val feedbackMessage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("flamme_label")
    val flammeLabel: String?,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
    @JsonProperty("recap_emoji")
    val recapEmoji: String,
    @JsonProperty("recap_label")
    val recapLabel: String,
)

@JsonIgnoreProperties("createdAt", "updatedAt", "nom_strapi")
data class StrapiConsultationReponseCommanditaire(
    @JsonProperty("slug")
    val slug: String,
    @JsonProperty("template_partage")
    val templatePartage: String,
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
    @JsonProperty("feedback_message")
    val feedbackMessage: String,
    @JsonProperty("historique_titre")
    val historiqueTitre: String,
    @JsonProperty("historique_call_to_action")
    val historiqueCallToAction: String,
    @JsonProperty("flamme_label")
    val flammeLabel: String?,
    @JsonProperty("sections")
    val sections: List<StrapiConsultationSection>,
    @JsonProperty("recap_emoji")
    val recapEmoji: String,
    @JsonProperty("recap_label")
    val recapLabel: String,
)
