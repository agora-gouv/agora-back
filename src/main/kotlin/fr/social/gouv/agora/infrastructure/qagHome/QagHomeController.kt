package fr.social.gouv.agora.infrastructure.qagHome

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.social.gouv.agora.usecase.qagPreview.QagPreviewListUseCaseV2
import fr.social.gouv.agora.usecase.responseQag.ResponseQagPreviewListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagHomeController(
    private val responseQagPreviewListUseCase: ResponseQagPreviewListUseCase,
    private val getQagPreviewListUseCase: QagPreviewListUseCaseV2,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {
    @GetMapping("/qags")
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
        val responseQagPreviewList = responseQagPreviewListUseCase.getResponseQagPreviewList()

        return ResponseEntity.ok().body(
            qagHomeJsonMapper.toResponsesJson(
                incomingResponses = responseQagPreviewList.incomingResponses,
                responses = responseQagPreviewList.responses,
            )
        )
    }
}