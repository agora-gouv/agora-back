package fr.gouv.agora.infrastructure.qagHome

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

data class QagResponsesJson(
    @JsonProperty("incomingResponses")
    val incomingResponses: List<IncomingResponseQagPreviewJson>,
    @JsonProperty("responses")
    val responsesList: List<ResponseQagPreviewJson>,
)

data class IncomingResponseQagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("support")
    val support: SupportQagJson,
    @JsonProperty("order")
    val order: Int,
    @JsonProperty("previousMondayDate")
    val previousMondayDate: String,
    @JsonProperty("nextMondayDate")
    val nextMondayDate: String,
)

data class ResponseQagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("author")
    val author: String,
    @JsonProperty("authorPortraitUrl")
    val authorPortraitUrl: String,
    @JsonProperty("responseDate")
    val responseDate: String,
    @JsonProperty("order")
    val order: Int,
)

data class ResponseQagPreviewWithoutOrderJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("author")
    val author: String,
    @JsonProperty("authorPortraitUrl")
    val authorPortraitUrl: String,
    @JsonProperty("responseDate")
    val responseDate: String,
)

data class QagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
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
    @JsonProperty("isAuthor")
    val isAuthor: Boolean,
)

data class QagPreviewListJson(
    @JsonProperty("results")
    val results: List<QagPreviewJson>,
)

data class HeaderQagJson(
    @JsonProperty("headerId")
    val headerId: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("message")
    val message: String,
)

data class QagAskStatusJson(
    @JsonProperty("askQagErrorText")
    val askQagErrorText: String?,
)
