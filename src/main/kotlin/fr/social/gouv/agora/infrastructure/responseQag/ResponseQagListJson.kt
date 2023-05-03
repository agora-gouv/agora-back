package fr.social.gouv.agora.infrastructure.responseQag

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.qag.QagJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJson

data class ResponseQagListJson(
    @JsonProperty("responses")
    val responsesList: List<ResponseQagPreviewJson>,
    @JsonProperty("qags")
    val qagsList: List<QagJson>,
)

data class ResponseQagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("author")
    val author: String,
    @JsonProperty("authorPortraitUrl")
    val authorPortraitUrl: String,
    @JsonProperty("responseDate")
    val responseDate: String,
)