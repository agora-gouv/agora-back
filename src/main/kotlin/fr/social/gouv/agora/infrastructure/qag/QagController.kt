package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.usecase.qag.GetQagUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagController(
    private val getQagUseCase: GetQagUseCase,
    private val mapper: QagJsonMapper,
) {

    @GetMapping("/qags/{qagId}")
    fun getQagDetails(@PathVariable qagId: String): HttpEntity<*> {
        return getQagUseCase.getQag(qagId)?.let { qag ->
            ResponseEntity.ok(mapper.toJson(qag))
        } ?: ResponseEntity.EMPTY
    }

}