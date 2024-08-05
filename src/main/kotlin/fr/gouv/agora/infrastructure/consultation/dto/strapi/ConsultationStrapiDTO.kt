package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiDataList
import fr.gouv.agora.infrastructure.common.StrapiDataNullable
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
    val flammeLabel: String?,
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
    val consultationContenuAVenir: StrapiDataNullable<StrapiConsultationAVenir>,
) {
    fun getLatestUpdateDate(now: LocalDateTime): LocalDateTime? {
        return listOfNotNull(
            *consultationContenuAutres.data.map { it.attributes.datetimePublication }.toTypedArray(),
            dateDeDebut,
        ).filter { it.isBefore(now) }.maxOrNull()
    }

    fun getNextQuestionId(question: StrapiConsultationQuestion): String? {
        return if (question.numeroQuestionSuivante != null) {
            questions.firstOrNull { question.numeroQuestionSuivante!! == it.numero }?.id
        } else {
            questions.firstOrNull { question.numero + 1 == it.numero }?.id
        }
    }

    fun getContenuIdBySlug(slug: String): String? {
        return if (contenuAvantReponse.data.attributes.slug == slug) {
            contenuAvantReponse.data.id
        } else if (contenuApresReponseOuTerminee.data.attributes.slug == slug) {
            contenuApresReponseOuTerminee.data.id
        } else {
            consultationContenuAutres.data.firstOrNull { it.attributes.slug == slug }?.id
        }
    }
}
