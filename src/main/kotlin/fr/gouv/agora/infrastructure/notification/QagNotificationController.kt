package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import io.swagger.v3.oas.annotations.Operation
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
class QagNotificationController(
    private val sendQagNotificationUseCase: SendQagNotificationUseCase,
) {
    @Operation(
        summary = "Envoyer une notification pour informer qu'une réponse du gouvernement est disponible", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request : L'id de la question fourni n'existe pas",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/notifyQag/{qagId}")
    fun notifyAllQagNotification(
        @RequestParam("title", defaultValue = "Une nouvelle consultation est disponible") title: String,
        @RequestParam("description", defaultValue = "Venez découvrir la nouvelle consultation sur l'éducation") description: String,
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
