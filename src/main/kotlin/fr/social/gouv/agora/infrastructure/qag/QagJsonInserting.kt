package fr.social.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
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