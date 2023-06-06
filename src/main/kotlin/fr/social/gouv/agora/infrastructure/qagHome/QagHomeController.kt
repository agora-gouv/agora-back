package fr.social.gouv.agora.infrastructure.qagHome

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.social.gouv.agora.usecase.qag.GetQagLatestPreviewListUseCase
import fr.social.gouv.agora.usecase.qag.GetQagPopularPreviewListUseCase
import fr.social.gouv.agora.usecase.qag.GetQagSupportedPreviewListUseCase
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
    private val getQagPopularPreviewListUseCase: GetQagPopularPreviewListUseCase,
    private val getQagLatestPreviewListUseCase: GetQagLatestPreviewListUseCase,
    private val getQagSupportedPreviewListUseCase: GetQagSupportedPreviewListUseCase,
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
        val qagPopularPreviewList = getQagPopularPreviewListUseCase.getQagPopularPreviewList(
            userId = userId,
            thematiqueId = thematiqueId,
        )
        val qagLatestPreviewList = getQagLatestPreviewListUseCase.getQagLatestPreviewList(
            userId = userId,
            thematiqueId = thematiqueId,
        )
        val qagSupportedPreviewList = getQagSupportedPreviewListUseCase.getQagSupportedPreviewList(
            userId = userId,
            thematiqueId = thematiqueId,
        )
        return ResponseEntity.ok()
            .body(
                qagHomeJsonMapper.toJson(
                    responseQagList = responseQagPreviewList,
                    qagPopularList = qagPopularPreviewList,
                    qagLatestList = qagLatestPreviewList,
                    qagSupportedList = qagSupportedPreviewList,
                    qagErrorText = getQagErrorTextUseCase.getGetQagErrorText(userId),
                )
            )
    }
}