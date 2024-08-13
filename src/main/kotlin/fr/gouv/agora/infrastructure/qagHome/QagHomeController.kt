package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "QaG")
class QagHomeController(
    private val responseQagPreviewListUseCase: ResponseQagPreviewListUseCase,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {

    @Operation(summary = "Get QaG Responses")
    @GetMapping("/qags/responses")
    fun getQagResponses(): ResponseEntity<QagResponsesJson> {
        return ResponseEntity.ok().body(
            qagHomeJsonMapper.toResponsesJson(responseQagPreviewListUseCase.getResponseQagPreviewList())
        )
    }

}
