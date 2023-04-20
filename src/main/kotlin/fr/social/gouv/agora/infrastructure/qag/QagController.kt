package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.usecase.qag.GetQagUseCase
import fr.social.gouv.agora.usecase.supportQag.GetSupportQagUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagController(
    private val getQagUseCase: GetQagUseCase,
    private val getSupportQagUseCase: GetSupportQagUseCase,
    private val mapper: QagJsonMapper,
) {

    @GetMapping("/qags/{qagId}")
    fun getQagDetails(
        @RequestHeader("deviceId") deviceId: String,
        @PathVariable qagId: String,
    ): HttpEntity<*> {
        return getQagUseCase.getQag(qagId)?.let { qag ->
            ResponseEntity.ok(
                mapper.toJson(
                    qag = qag,
                    supportQag = getSupportQagUseCase.getSupportQag(qagId, deviceId),
                )
            )
        } ?: ResponseEntity.EMPTY
    }

}