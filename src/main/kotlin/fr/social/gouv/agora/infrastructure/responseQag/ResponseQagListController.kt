package fr.social.gouv.agora.infrastructure.responseQag

import fr.social.gouv.agora.usecase.responseQag.GetResponseQagPreviewListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ResponseQagListController(
    private val getResponseQagPreviewListUseCase: GetResponseQagPreviewListUseCase,
    private val responseQagListJsonMapper: ResponseQagListJsonMapper,
) {
    @GetMapping("/qags")
    fun getResponseQagPreviewList(): ResponseEntity<ResponseQagListJson> {
        val responseQagPreviewList = getResponseQagPreviewListUseCase.getResponseQagPreviewList()
        return ResponseEntity.ok()
            .body(responseQagListJsonMapper.toJson(responseQagPreviewList ?: emptyList()))
    }
}