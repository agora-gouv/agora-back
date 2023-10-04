package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.usecase.notification.SendQagNotificationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagNotificationController(
    private val sendQagNotificationUseCase: SendQagNotificationUseCase,
) {

    @GetMapping("/admin/notifyQag/{qagId}")
    fun notifyAllQagNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("qagId") qagId: String,
    ): ResponseEntity<*> {
        sendQagNotificationUseCase.sendNotificationQagUpdate(
            title = title,
            description = description,
            qagId = qagId,
        )
        return ResponseEntity.ok().body(Unit)
    }

}