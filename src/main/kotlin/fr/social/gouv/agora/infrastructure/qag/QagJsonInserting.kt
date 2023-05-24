package fr.social.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonProperty

data class QagJsonInserting(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("thematiqueId")
    val thematiqueId: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("author")
    val author: String,
)