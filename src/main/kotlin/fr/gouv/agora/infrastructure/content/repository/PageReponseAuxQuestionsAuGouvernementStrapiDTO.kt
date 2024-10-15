package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("createdAt", "updatedAt")
data class PageReponseAuxQuestionsAuGouvernementStrapiDTO(
    @JsonProperty(value = "information_reponse_a_venir_bottomsheet")
    val informationReponseAVenirBottomsheet: String,
)
