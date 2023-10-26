package fr.gouv.agora.infrastructure.thematique

import com.fasterxml.jackson.annotation.JsonProperty

data class ThematiquesJson(
    @JsonProperty("thematiques")
    val thematiques: List<ThematiqueJson>,
)

data class ThematiqueJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("picto")
    val picto: String,
)

data class ThematiqueNoIdJson(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("picto")
    val picto: String,
)
