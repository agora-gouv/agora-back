package fr.social.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class QagJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("thematiqueId")
    val thematiqueId: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("date")
    val date: Date,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("supportCount")
    val supportCount: Int,
)
