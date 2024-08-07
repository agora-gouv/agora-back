package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class QagNotificationController(
    private val sendQagNotificationUseCase: SendQagNotificationUseCase,
) {
    @Operation(summary = "Admin Qag Notification")
    @GetMapping("/admin/notifyQag/{qagId}")
    fun notifyAllQagNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("qagId") qagId: String,
    ): ResponseEntity<*> {
        val result = sendQagNotificationUseCase.sendNotificationQagUpdate(
            title = title,
            description = description,
            qagId = qagId,
        )

        return when (result) {
            NotificationResult.SUCCESS -> ResponseEntity.ok().body(Unit)
            NotificationResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
        }
    }

}
