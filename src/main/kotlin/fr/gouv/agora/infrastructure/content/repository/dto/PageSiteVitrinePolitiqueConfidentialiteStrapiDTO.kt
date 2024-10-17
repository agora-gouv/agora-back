package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSiteVitrinePolitiqueConfidentialiteStrapiDTO (
    @JsonProperty("politique_de_confidentialite")
    val politiqueDeConfidentialite: String,
)
