package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("createdAt", "updatedAt")
data class PageQuestionsAuGouvernementStrapiDTO(
    @JsonProperty(value = "information_bottomsheet")
    val informationBottomsheet: String,
    @JsonProperty(value = "nombre_de_questions")
    val nombreDeQuestions: String,
)
