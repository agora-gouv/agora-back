package fr.social.gouv.agora.infrastructure.qagSimilar

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.qag.SupportQagJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import java.util.*

data class QagSimilarJson(
    @JsonProperty("similarQags")
    val similarQags: List<QagWithDescriptionJson>,
)

data class QagWithDescriptionJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
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

data class QagHasSimilarJson(
    @JsonProperty("hasSimilar")
    val hasSimilar: Boolean,
)
