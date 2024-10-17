package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSiteVitrineQuestionAuGouvernementStrapiDTO (
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("sous_titre")
    val sousTitre: String,
    @JsonProperty("texte_soutien")
    val texteSoutien: String,
)
