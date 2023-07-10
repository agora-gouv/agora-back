package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.usecase.notification.SendConsultationNotificationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class NewConsultationNotificationController(
    private val sendConsultationNotificationUseCase: SendConsultationNotificationUseCase,
) {
    companion object {
        private const val CONSULTATION_DETAILS_NOTIFICATION_TYPE = "consultationDetails"
        private const val CONSULTATION_RESULTS_NOTIFICATION_TYPE = "consultationResults"
    }

    @GetMapping("/admin/notifyNewConsultation/{consultationId}")
    fun newConsultationNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @RequestParam("type") type: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val responseResult = when (type) {
            CONSULTATION_DETAILS_NOTIFICATION_TYPE -> sendConsultationNotificationUseCase.sendNewConsultationNotification(
                title = title,
                description = description,
                consultationId = consultationId
            )

            else -> return ResponseEntity.badRequest().body("Type erroné")
        }
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
        @RequestParam("type") type: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val responseResult = when (type) {
            CONSULTATION_RESULTS_NOTIFICATION_TYPE -> sendConsultationNotificationUseCase.sendConsultationUpdateNotification(
                title = title,
                description = description,
                consultationId = consultationId
            )

            else -> return ResponseEntity.badRequest().body("Type erroné")
        }
        return when (responseResult) {
            null -> ResponseEntity.badRequest().body("Erreur d'envoi de la notification")
            else -> ResponseEntity.ok()
                .body(
                    "Notification envoyée avec succès à : ${responseResult.first} / ${responseResult.second} utilisateurs"
                )
        }
    }
}