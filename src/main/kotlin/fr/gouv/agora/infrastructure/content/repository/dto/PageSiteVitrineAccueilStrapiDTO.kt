package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText

@JsonIgnoreProperties("createdAt", "updatedAt")
data class PageSiteVitrineAccueilStrapiDTO(
    @JsonProperty("titre_header")
    val titreHeader: String,
    @JsonProperty("sous_titre_header")
    val sousTitreHeader: String,
    @JsonProperty("titre_body")
    val titreBody: String,
    @JsonProperty("description_body")
    val descriptionBody: String,
    @JsonProperty("texte_image_1")
    val texteImage1: List<StrapiRichText>,
    @JsonProperty("texte_image_2")
    val texteImage2: List<StrapiRichText>,
    @JsonProperty("texte_image_3")
    val texteImage3: List<StrapiRichText>,
)
