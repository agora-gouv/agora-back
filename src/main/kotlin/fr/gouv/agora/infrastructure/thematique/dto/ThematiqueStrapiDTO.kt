package fr.gouv.agora.infrastructure.thematique.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class ThematiqueStrapiDTO(
    @JsonProperty(value = "label")
    val label: String,
    @JsonProperty(value = "pictogramme")
    val pictogramme: String,
    @JsonProperty(value = "id_base_de_donnees")
    val databaseId: String,
)
