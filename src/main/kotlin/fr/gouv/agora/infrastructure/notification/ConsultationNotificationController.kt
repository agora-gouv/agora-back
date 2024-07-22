package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.SendConsultationNotificationUseCase
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
@Tag(name = "Admin")
class ConsultationNotificationController(
    private val sendConsultationNotificationUseCase: SendConsultationNotificationUseCase,
) {
    @Operation(summary = "Admin Notification Nouvelle Consultation")
    @GetMapping("/admin/notifyNewConsultation/{consultationId}")
    fun newConsultationNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val result = sendConsultationNotificationUseCase.sendNewConsultationNotification(
            title = title,
            description = description,
            consultationId = consultationId,
        )
        return when (result) {
            NotificationResult.SUCCESS -> ResponseEntity.ok().body(Unit)
            NotificationResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
        }
    }

    @Operation(summary = "Admin Notification Update Consultation")
    @GetMapping("/admin/notifyConsultationUpdate/{consultationId}")
    fun consultationUpdateNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val result = sendConsultationNotificationUseCase.sendConsultationUpdateNotification(
            title = title,
            description = description,
            consultationId = consultationId
        )
        return when (result) {
            NotificationResult.SUCCESS -> ResponseEntity.ok().body(Unit)
            NotificationResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
        }
    }

    @Operation(summary = "Admin Notification Consultation Deadline")
    @GetMapping("/admin/notifyConsultationDeadline/{consultationId}")
    fun consultationDeadlineNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val result = sendConsultationNotificationUseCase.sendConsultationDeadlineNotification(
            title = title,
            description = description,
            consultationId = consultationId
        )
        return when (result) {
            NotificationResult.SUCCESS -> ResponseEntity.ok().body(Unit)
            NotificationResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
        }
    }
}
