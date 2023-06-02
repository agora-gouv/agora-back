package fr.social.gouv.agora.infrastructure.qag

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class QagModeratingHomeJson(
    @JsonProperty("totalNumber")
    val totalNumber: Int,
    @JsonProperty("qagsToModerate")
    val qagsToModerate: List<QagModeratingJson>,
)

data class QagModeratingJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("date")
    val date: String,
    @JsonProperty("support")
    val support: SupportQagJson,
)

data class QagModeratingStatusJson (
    @JsonProperty("isAccepted")
    val isAccepted: Boolean,
)
