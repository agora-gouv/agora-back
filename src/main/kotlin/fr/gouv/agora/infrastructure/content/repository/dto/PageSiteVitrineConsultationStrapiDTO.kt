package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

data class PageSiteVitrineConsultationStrapiDTO (
    @JsonProperty("donnez_votre_avis")
    val donnezVotreAvis: List<StrapiRichText>,
)
