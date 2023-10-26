package fr.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonProperty

data class QagInsertingJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("thematiqueId")
    val thematiqueId: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("author")
    val author: String,
)

data class QagInsertionResultJson(
    @JsonProperty("qagId")
    val qagId: String,
)