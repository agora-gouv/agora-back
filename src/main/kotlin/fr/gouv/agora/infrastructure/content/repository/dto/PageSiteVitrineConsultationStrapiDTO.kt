package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSiteVitrineConsultationStrapiDTO (
    @JsonProperty("donnez_votre_avis")
    val donnezVotreAvis: String,
)
