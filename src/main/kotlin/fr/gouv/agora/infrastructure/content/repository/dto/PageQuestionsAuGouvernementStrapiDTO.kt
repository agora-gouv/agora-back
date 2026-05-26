package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

@JsonIgnoreProperties("createdAt", "updatedAt")
data class PageQuestionsAuGouvernementStrapiDTO(
    @JsonProperty(value = "information_bottomsheet")
    val informationBottomsheet: String,
    @JsonProperty(value = "nombre_de_questions")
    val nombreDeQuestions: String,
    @JsonProperty(value = "programme_du_mois")
    val programmeDuMois: List<StrapiRichText>?,
    @JsonProperty(value = "comment_ca_marche")
    val commentCaMarche: String?,
)
