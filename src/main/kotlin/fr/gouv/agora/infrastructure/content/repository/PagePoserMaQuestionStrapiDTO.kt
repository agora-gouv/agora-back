package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

@JsonIgnoreProperties("createdAt", "updatedAt")
data class PagePoserMaQuestionStrapiDTO(
    @JsonProperty(value = "texte_regles")
    val texteRegles: List<StrapiRichText>,
)
