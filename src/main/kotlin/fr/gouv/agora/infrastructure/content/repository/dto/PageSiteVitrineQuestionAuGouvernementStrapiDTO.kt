package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

data class PageSiteVitrineQuestionAuGouvernementStrapiDTO (
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("sous_titre")
    val sousTitre: String,
    @JsonProperty("texte_soutien")
    val texteSoutien: List<StrapiRichText>,
)
