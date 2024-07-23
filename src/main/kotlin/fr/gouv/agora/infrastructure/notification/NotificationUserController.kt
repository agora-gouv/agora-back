package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.SendUserNotificationUseCase
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
class NotificationUserController(
    private val sendUserNotificationUseCase: SendUserNotificationUseCase
) {
    @Operation(summary = "Admin User Notification")
    @GetMapping("/admin/notifyUser/{userId}")
    fun notifyUser(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("userId") userId: String,
    ): ResponseEntity<*> {
        val result = sendUserNotificationUseCase.execute(
            title = title,
            description = description,
            userId = userId
        )

        return when (result) {
            NotificationResult.SUCCESS -> ResponseEntity.ok().body(Unit)
            NotificationResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
        }
    }
}
