package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

data class PageSiteVitrineConditionGeneralesStrapiDTO(
    @JsonProperty("conditions_generales_d_utilisation")
    val conditionsGeneralesDUtilisation: List<StrapiRichText>,
)
