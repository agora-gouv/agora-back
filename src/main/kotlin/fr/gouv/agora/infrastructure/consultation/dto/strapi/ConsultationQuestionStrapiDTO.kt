package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.agora.infrastructure.common.StrapiRichText

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
    JsonSubTypes.Type(
        value = StrapiConsultationQuestionConditionnelle::class,
        name = "question-de-consultation.question-conditionnelle"
    ),
)
@JsonIgnoreProperties("__component", "createdAt", "updatedAt", "publishedAt")
sealed interface StrapiConsultationQuestion {
    val id: String
    val numero: Int
    val numeroQuestionSuivante: Int?
}

data class StrapiConsultationQuestionChoixMultiples(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    override val numero: Int,
    @JsonProperty("nombre_maximum_de_choix")
    val nombreMaximumDeChoix: Int,
    @JsonProperty("choix")
    val choix: List<StrapiConsultationChoixSimple>,
    @JsonProperty("popup_explication")
    val popupExplication: List<StrapiRichText>?,
    @JsonProperty("question_suivante")
    override val numeroQuestionSuivante: Int?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionChoixUnique(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    override val numero: Int,
    @JsonProperty("choix")
    val choix: List<StrapiConsultationChoixSimple>,
    @JsonProperty("popup_explication")
    val popupExplication: List<StrapiRichText>?,
    @JsonProperty("question_suivante")
    override val numeroQuestionSuivante: Int?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionOuverte(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    override val numero: Int,
    @JsonProperty("popup_explication")
    val popupExplication: List<StrapiRichText>?,
    @JsonProperty("question_suivante")
    override val numeroQuestionSuivante: Int?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionDescription(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    override val numero: Int,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
    @JsonProperty("question_suivante")
    override val numeroQuestionSuivante: Int?,
) : StrapiConsultationQuestion

data class StrapiConsultationQuestionConditionnelle(
    @JsonProperty("id")
    override val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("numero")
    override val numero: Int,
    @JsonProperty("choix")
    val choix: List<StrapiConsultationChoixConditionnel>,
    @JsonProperty("popup_explication")
    val popupExplication: List<StrapiRichText>?,
    override val numeroQuestionSuivante: Int? = null,
) : StrapiConsultationQuestion


data class StrapiConsultationChoixSimple(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ouvert")
    val ouvert: Boolean,
)

data class StrapiConsultationChoixConditionnel(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ouvert")
    val ouvert: Boolean,
    @JsonProperty("numero_de_la_question_suivante")
    val numeroDeLaQuestionSuivante: Int
)
