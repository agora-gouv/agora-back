package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

data class PageSiteVitrinePolitiqueConfidentialiteStrapiDTO (
    @JsonProperty("politique_de_confidentialite")
    val politiqueDeConfidentialite: List<StrapiRichText>,
)
