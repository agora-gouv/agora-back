package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSiteVitrineConditionGeneralesStrapiDTO(
    @JsonProperty("conditions_generales_d_utilisation")
    val conditionsGeneralesDUtilisation: String,
)
