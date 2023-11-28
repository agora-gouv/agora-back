package fr.gouv.agora.infrastructure.qagHome

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QagPreviewsJson(
    @JsonProperty("qags")
    val qagList: QagListJson,
    @JsonProperty("askQagErrorText")
    val askQagErrorText: String?,
    @JsonProperty("popup")
    val qagPopup: QagPopupJson?,
)

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
)

data class QagListJson(
    @JsonProperty("popular")
    val popular: List<QagPreviewJson>,
    @JsonProperty("latest")
    val latest: List<QagPreviewJson>,
    @JsonProperty("supporting")
    val supporting: List<QagPreviewJson>,
)

data class QagPreviewJson(
    @JsonProperty("qagId")
    val qagId: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("title")
    val title: String,
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

data class QagPopupJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
)

data class HeaderJson(
    @JsonProperty("headerId")
    val headerId: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("message")
    val message: String,
)
