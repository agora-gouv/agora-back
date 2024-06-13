package fr.gouv.agora.infrastructure.thematique.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StrapiThematiqueDTO(
    @JsonProperty(value = "label")
    val label: String,
    @JsonProperty(value = "pictogramme")
    val pictogramme: String,
    @JsonProperty(value = "id_base_de_donnees")
    val databaseId: String,
)
