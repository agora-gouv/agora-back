package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.usecase.notification.SendNewConsultationNotificationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class NewConsultationNotificationController(
    private val sendNewConsultationNotificationUseCase: SendNewConsultationNotificationUseCase,
) {
    @GetMapping("/admin/consultations/{consultationId}")
    fun newConsultationNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @RequestParam("type") type: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val successResponseNumber = sendNewConsultationNotificationUseCase.sendNewConsultationNotification(
            title = title,
            description = description,
            type = type,
            consultationId = consultationId
        )
        return when (successResponseNumber) {
            null -> ResponseEntity.badRequest().body("Erreur d'envoi de la notification")
            else -> ResponseEntity.ok().body("Notification envoyée avec succès à : $successResponseNumber")
        }
    }}