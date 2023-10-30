package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.usecase.notification.SendConsultationNotificationUseCase
import fr.social.gouv.agora.usecase.notification.repository.NotificationResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class ConsultationNotificationController(
    private val sendConsultationNotificationUseCase: SendConsultationNotificationUseCase,
) {
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
}