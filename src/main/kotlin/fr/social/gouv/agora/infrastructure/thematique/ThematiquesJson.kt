package fr.social.gouv.agora.infrastructure.thematique

import com.fasterxml.jackson.annotation.JsonProperty

data class ThematiquesJson(
    @JsonProperty("thematiques")
    val thematiques: List<ThematiqueJson>,
)

data class ThematiqueJson(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("label")
    val label: String,
    @JsonProperty("picto")
    val picto: String,
    @JsonProperty("color")
    val color: String,
)
