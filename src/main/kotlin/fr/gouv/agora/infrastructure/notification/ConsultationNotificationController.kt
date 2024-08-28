package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.SendConsultationNotificationUseCase
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class ConsultationNotificationController(
    private val sendConsultationNotificationUseCase: SendConsultationNotificationUseCase,
) {
    @Operation(
        summary = "Envoyer une notification pour avertir qu'une nouvelle Consultation est disponible", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request : L'id de consultation fourni n'existe pas",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/notifyNewConsultation/{consultationId}")
    fun newConsultationNotification(
        @RequestParam("title", defaultValue = "Une nouvelle consultation est disponible") title: String,
        @RequestParam("description", defaultValue = "Venez découvrir la nouvelle consultation sur l'éducation") description: String,
        @PathVariable("consultationId") @Parameter(example = "db0f2d59-7962-43ff-a69f-878204b7be95") consultationId: String,
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

    @Operation(
        summary = "Envoyer une notification pour avertir qu'une nouvelle Consultation a été mise à jour", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request : L'id de consultation fourni n'existe pas",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/notifyConsultationUpdate/{consultationId}")
    fun consultationUpdateNotification(
        @RequestParam("title", defaultValue = "L'analyse de la consultation sur l'éducation est disponible") title: String,
        @RequestParam("description", defaultValue = "Venez découvrir ce que les gens ont pensé de la consultation sur l'éducation") description: String,
        @PathVariable("consultationId") @Parameter(example = "db0f2d59-7962-43ff-a69f-878204b7be95") consultationId: String,
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

    @Operation(
        summary = "Envoyer une notification pour rappeler la date de fin d'une consultation", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request : L'id de consultation fourni n'existe pas",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/notifyConsultationDeadline/{consultationId}")
    fun consultationDeadlineNotification(
        @RequestParam("title", defaultValue = "Attention, la consultation sur le l'éducation se termine le 29 septembre") title: String,
        @RequestParam("description", defaultValue = "Répondez à la consultation sur l'éducation avant qu'elle ne se termine") description: String,
        @PathVariable("consultationId") @Parameter(example = "db0f2d59-7962-43ff-a69f-878204b7be95") consultationId: String,
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
