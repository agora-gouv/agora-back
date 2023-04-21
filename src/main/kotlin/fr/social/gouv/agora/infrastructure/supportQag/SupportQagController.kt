package fr.social.gouv.agora.infrastructure.supportQag

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.supportQag.DeleteSupportQagUseCase
import fr.social.gouv.agora.usecase.supportQag.InsertSupportQagUseCase
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class SupportQagController(
    private val insertSupportQagUseCase: InsertSupportQagUseCase,
    private val deleteSupportQagUseCase: DeleteSupportQagUseCase,
) {
    @PostMapping("/qags/{qagId}/support")
    fun insertSupportQag(@RequestHeader("deviceId") deviceId: String, @PathVariable qagId: String): HttpEntity<*> {
        val insertResult = insertSupportQagUseCase.insertSupportQag(
            SupportQagInserting(
                userId = deviceId,
                qagId = qagId,
            )
        )
        return if (insertResult == SupportQagResult.SUCCESS) {
            ResponseEntity.status(200).body("")
        } else ResponseEntity.status(400).body("")
    }

    @DeleteMapping("/qags/{qagId}/support")
    fun deleteSupportQag(@RequestHeader("deviceId") deviceId: String, @PathVariable qagId: String): HttpEntity<*> {
        val deleteResult = deleteSupportQagUseCase.deleteSupportQag(
            SupportQagDeleting(
                userId = deviceId,
                qagId = qagId,
            )
        )
        return if (deleteResult == SupportQagResult.SUCCESS) {
            ResponseEntity.status(200).body("")
        } else ResponseEntity.status(400).body("")
    }
}