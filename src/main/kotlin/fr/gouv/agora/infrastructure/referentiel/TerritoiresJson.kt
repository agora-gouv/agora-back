package fr.gouv.agora.infrastructure.referentiel

import com.fasterxml.jackson.annotation.JsonProperty

data class TerritoiresJson(
    @JsonProperty("regions")
    val regions: List<RegionJson>,
    @JsonProperty("pays")
    val pays: List<String>
)
