package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagHomeController(
    private val responseQagPreviewListUseCase: ResponseQagPreviewListUseCase,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {

    @GetMapping("/qags/responses")
    fun getQagResponses(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<QagResponsesJson> {
        return ResponseEntity.ok().body(
            qagHomeJsonMapper.toResponsesJson(responseQagPreviewListUseCase.getResponseQagPreviewList())
        )
    }

}