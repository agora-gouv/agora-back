package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSiteVitrineMentionsLegalesStrapiDTO (
    @JsonProperty("mentions_legales")
    val mentionsLegales: String,
)
