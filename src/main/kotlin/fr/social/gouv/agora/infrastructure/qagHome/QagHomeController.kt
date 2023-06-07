package fr.social.gouv.agora.infrastructure.qagHome

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.social.gouv.agora.usecase.qagPreview.GetQagPreviewListUseCase
import fr.social.gouv.agora.usecase.responseQag.GetResponseQagPreviewListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagHomeController(
    private val getResponseQagPreviewListUseCase: GetResponseQagPreviewListUseCase,
    private val getQagPreviewListUseCase: GetQagPreviewListUseCase,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {
    @GetMapping("/qags")
    fun getQagHome(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("thematiqueId") thematiqueId: String?,
    ): ResponseEntity<QagHomeJson> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val responseQagPreviewList = getResponseQagPreviewListUseCase.getResponseQagPreviewList()
        val qagPreviewList = getQagPreviewListUseCase.getQagPreviewList(userId = userId, thematiqueId = thematiqueId)

        return ResponseEntity.ok()
            .body(
                qagHomeJsonMapper.toJson(
                    responseQagList = responseQagPreviewList,
                    qagPopularList = qagPreviewList.popularPreviewList,
                    qagLatestList = qagPreviewList.latestPreviewList,
                    qagSupportedList = qagPreviewList.supportedPreviewList,
                    qagErrorText = getQagErrorTextUseCase.getGetQagErrorText(userId),
                )
            )
    }
}