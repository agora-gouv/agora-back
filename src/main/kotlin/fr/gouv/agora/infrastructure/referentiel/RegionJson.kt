package fr.gouv.agora.infrastructure.referentiel

import com.fasterxml.jackson.annotation.JsonProperty

data class RegionJson(
    @JsonProperty("region")
    val region: String,
    @JsonProperty("departements")
    val departements: List<String>,
)
