package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

data class PageSiteVitrineMentionsLegalesStrapiDTO (
    @JsonProperty("mentions_legales")
    val mentionsLegales: List<StrapiRichText>,
)
