package fr.gouv.agora.infrastructure.content.repository.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSiteVitrineDeclarationAccessibiliteStrapiDTO (
    @JsonProperty("declaration")
    val declaration: String,
)
