package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.gouv.agora.usecase.qagPreview.QagPreviewListUseCase
import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagHomeController(
    private val responseQagPreviewListUseCase: ResponseQagPreviewListUseCase,
    private val getQagPreviewListUseCase: QagPreviewListUseCase,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {
    @GetMapping("/qags")
    @Deprecated("Should use GET /v2/qags instead")
    fun getQagPreviews(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("thematiqueId") thematiqueId: String?,
    ): ResponseEntity<QagPreviewsJson> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val qagPreviewList = getQagPreviewListUseCase.getQagPreviewList(
            userId = userId,
            thematiqueId = thematiqueId.takeUnless { it.isNullOrBlank() },
        )

        return ResponseEntity.ok().body(
            qagHomeJsonMapper.toJson(
                qagPopularList = qagPreviewList.popularPreviewList,
                qagLatestList = qagPreviewList.latestPreviewList,
                qagSupportedList = qagPreviewList.supportedPreviewList,
                qagErrorText = getQagErrorTextUseCase.getGetQagErrorText(userId),
            )
        )
    }

    @GetMapping("/qags/responses")
    fun getQagResponses(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<QagResponsesJson> {
        return ResponseEntity.ok().body(
            qagHomeJsonMapper.toResponsesJson(responseQagPreviewListUseCase.getResponseQagPreviewList())
        )
    }
}