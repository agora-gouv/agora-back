package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.usecase.notification.SendConsultationNotificationUseCase
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
        val responseResult = sendConsultationNotificationUseCase.sendNewConsultationNotification(
            title = title,
            description = description,
            consultationId = consultationId
        )
        return when (responseResult) {
            null -> ResponseEntity.badRequest().body("Erreur d'envoi de la notification")
            else -> ResponseEntity.ok()
                .body(
                    "Notification envoyée avec succès à : ${responseResult.first} / ${responseResult.second} utilisateurs"
                )
        }
    }

    @GetMapping("/admin/notifyConsultationUpdate/{consultationId}")
    fun consultationUpdateNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val responseResult = sendConsultationNotificationUseCase.sendConsultationUpdateNotification(
            title = title,
            description = description,
            consultationId = consultationId
        )
        return when (responseResult) {
            null -> ResponseEntity.badRequest().body("Erreur d'envoi de la notification")
            else -> ResponseEntity.ok()
                .body(
                    "Notification envoyée avec succès à : ${responseResult.first} / ${responseResult.second} utilisateurs"
                )
        }
    }
}