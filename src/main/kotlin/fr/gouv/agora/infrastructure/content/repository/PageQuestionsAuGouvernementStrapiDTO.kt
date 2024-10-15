package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("createdAt", "updatedAt")
data class PageQuestionsAuGouvernementStrapiDTO(
    @JsonProperty(value = "information_bottomsheet")
    val informationBottomsheet: String,
)
