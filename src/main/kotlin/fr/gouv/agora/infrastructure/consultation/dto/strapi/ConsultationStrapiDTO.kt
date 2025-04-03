package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiDataList
import fr.gouv.agora.infrastructure.common.StrapiDataNullable
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiMediaVideo
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt")
data class ConsultationStrapiDTO(
    @JsonProperty(value = "titre_consultation")
    val titre: String,
    @JsonProperty(value = "slug")
    val slug: String,
    @JsonProperty(value = "datetime_de_debut")
    val dateDeDebut: LocalDateTime,
    @JsonProperty(value = "datetime_de_fin")
    val dateDeFin: LocalDateTime,
    @JsonProperty(value = "publishedAt")
    val publishedAt: LocalDateTime?,
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
    @JsonProperty(value = "thematique")
    val thematique: StrapiData<ThematiqueStrapiDTO>,
    @JsonProperty(value = "questions")
    val questions: List<StrapiConsultationQuestion>,
    @JsonProperty(value = "consultation_avant_reponse")
    val contenuAvantReponse: StrapiData<StrapiConsultationContenuAvantReponse>,
    @JsonProperty(value = "consultation_apres_reponse_ou_terminee")
    val contenuApresReponseOuTerminee: StrapiData<StrapiConsultationContenuApresReponse>,
    @JsonProperty("consultation_contenu_analyse_des_reponse")
    val consultationContenuAnalyseDesReponses: StrapiDataNullable<StrapiConsultationAnalyseDesReponses>,
    @JsonProperty("contenu_reponse_du_commanditaires")
    val consultationContenuReponseDuCommanditaire: StrapiDataNullable<StrapiConsultationReponseCommanditaire>,
    @JsonProperty("consultation_contenu_autres")
    val consultationContenuAutres: StrapiDataList<StrapiConsultationContenuAutre>,
    @JsonProperty("consultation_contenu_a_venir")
    val consultationContenuAVenir: StrapiDataNullable<StrapiConsultationAVenir>,
    @JsonProperty("territoire")
    val territoire: String,
    @JsonProperty("titre_page_web")
    val titrePageWeb: String,
    @JsonProperty("sous_titre_page_web")
    val sousTitrePageWeb: String,
    @JsonProperty(value = "image_de_couverture")
    val imageDeCouverture: StrapiDataNullable<StrapiMediaPicture>,
    @JsonProperty(value = "image_page_de_contenu")
    val imagePageDeContenu: StrapiDataNullable<StrapiMediaPicture>,
) {
    fun getImageDeCouverture(): String {
        return if (imageDeCouverture.data == null) urlImageDeCouverture
        else System.getenv("CMS_URL") + imageDeCouverture.data.attributes.formats.medium.url
    }

    fun getImagePageDeContenu(): String {
        return if (imagePageDeContenu.data == null) urlImagePageDeContenu
        else System.getenv("CMS_URL") + imagePageDeContenu.data.attributes.formats.medium.url
    }

    fun isPublished(): Boolean {
        return publishedAt != null
    }

    fun getLatestUpdateDate(now: LocalDateTime): LocalDateTime? {
        return listOfNotNull(
            *consultationContenuAutres.data.map { it.attributes.datetimePublication }.toTypedArray(),
            consultationContenuAnalyseDesReponses.data?.attributes?.datetimePublication,
            consultationContenuReponseDuCommanditaire.data?.attributes?.datetimePublication,
            dateDeDebut,
        ).filter { it.isBefore(now) }.maxOrNull()
    }

    fun getNextQuestionId(question: StrapiConsultationQuestion): String? {
        if (question.numeroQuestionSuivante == null) {
            return questions.firstOrNull { question.numero + 1 == it.numero }?.id
        }

        val numeroDerniereQuestion = 999
        if (question.numeroQuestionSuivante == numeroDerniereQuestion) return null

        return questions.firstOrNull { question.numeroQuestionSuivante == it.numero }?.id
    }

    private fun getLastContenuAutre(now: LocalDateTime): StrapiAttributes<StrapiConsultationContenuAutre>? {
        return consultationContenuAutres.data
            .filter { it.attributes.datetimePublication.isBefore(now) }
            .maxByOrNull { it.attributes.datetimePublication }
    }

    fun getFlammeLabel(now: LocalDateTime): String? {
        return this.getLastContenuAutre(now)?.attributes?.flammeLabel
            ?: consultationContenuReponseDuCommanditaire.data?.attributes?.flammeLabel
            ?: consultationContenuAnalyseDesReponses.data?.attributes?.flammeLabel
    }
}
