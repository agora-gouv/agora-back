package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class ConsultationStrapiDTO(
    @JsonProperty(value = "documentId")
    val documentId: String,
    @JsonProperty(value = "titre_consultation")
    val titre: String,
    @JsonProperty(value = "slug")
    val slug: String,
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
    @JsonProperty(value = "thematique")
    val thematique: ThematiqueStrapiDTO,
    @JsonProperty(value = "questions")
    val questions: List<StrapiConsultationQuestion>,
    @JsonProperty(value = "consultation_avant_reponse")
    val contenuAvantReponse: StrapiConsultationContenuAvantReponse,
    @JsonProperty(value = "consultation_apres_reponse_ou_terminee")
    val contenuApresReponseOuTerminee: StrapiConsultationContenuApresReponse,
    @JsonProperty("consultation_contenu_analyse_des_reponse")
    val consultationContenuAnalyseDesReponses: StrapiConsultationAnalyseDesReponses?,
    @JsonProperty("contenu_reponse_du_commanditaires")
    val consultationContenuReponseDuCommanditaire: StrapiConsultationReponseCommanditaire?,
    @JsonProperty("consultation_contenu_autres")
    val consultationContenuAutres: List<StrapiConsultationContenuAutre>,
    @JsonProperty("consultation_contenu_a_venir")
    val consultationContenuAVenir: StrapiConsultationAVenir?,
    @JsonProperty("territoire")
    val territoire: String,
    @JsonProperty("titre_page_web")
    val titrePageWeb: String,
    @JsonProperty("sous_titre_page_web")
    val sousTitrePageWeb: String,
    @JsonProperty(value = "image_de_couverture")
    val imageDeCouverture: StrapiMediaPicture?,
    @JsonProperty(value = "image_page_de_contenu")
    val imagePageDeContenu: StrapiMediaPicture?,
) {
    fun getImageCouverture(): String {
        return imageDeCouverture?.mediaUrl() ?: urlImageDeCouverture
    }

    fun getImagePageContenu(): String {
        return imagePageDeContenu?.mediaUrl() ?: urlImagePageDeContenu
    }

    fun getLatestUpdateDate(now: LocalDateTime): LocalDateTime? {
        return listOfNotNull(
            *consultationContenuAutres.map { it.datetimePublication }.toTypedArray(),
            consultationContenuAnalyseDesReponses?.datetimePublication,
            consultationContenuReponseDuCommanditaire?.datetimePublication,
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

    private fun getLastContenuAutre(now: LocalDateTime): StrapiConsultationContenuAutre? {
        return consultationContenuAutres
            .filter { it.datetimePublication.isBefore(now) }
            .maxByOrNull { it.datetimePublication }
    }

    fun getFlammeLabel(now: LocalDateTime): String? {
        return this.getLastContenuAutre(now)?.flammeLabel
            ?: consultationContenuReponseDuCommanditaire?.flammeLabel
            ?: consultationContenuAnalyseDesReponses?.flammeLabel
    }
}
