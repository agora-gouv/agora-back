package fr.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

data class PublicQagJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("date")
    val date: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("supportCount")
    val supportCount: Int,
)