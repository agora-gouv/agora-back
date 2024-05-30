package fr.gouv.agora.infrastructure.thematique.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StrapiThematiqueDTO(
    @JsonProperty(value = "label")
    val label: String,
    @JsonProperty(value = "pictogramme")
    val pictogramme: String,
)
