package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.SendUserNotificationUseCase
import fr.gouv.agora.usecase.notification.SendUsersNotificationUseCase
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
class NotificationUserController(
    private val sendUserNotificationUseCase: SendUserNotificationUseCase,
    private val sendUsersNotificationUseCase: SendUsersNotificationUseCase,
) {
    @Operation(
        summary = "Envoyer une notification à un utilisateur précis", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request : L'id d'utilisateur' fourni n'existe pas",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/notifyUser/{userId}")
    fun notifyUser(
        @RequestParam("title", defaultValue = "Une nouvelle consultation est disponible") title: String,
        @RequestParam("description", defaultValue = "Venez découvrir la nouvelle consultation sur l'éducation") description: String,
        @PathVariable("userId") @Parameter(example = "a8480034-d383-4b43-8fd7-3df10d3ce41f") userId: String,
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

    @Operation(
        summary = "Envoyer une notification à tous les utilisateurs", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "La requête est incorrecte",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/notififyAllUsers")
    fun notifyAllUsers(
        @RequestParam("title", defaultValue = "Une nouvelle consultation est disponible") title: String,
        @RequestParam("description", defaultValue = "Venez découvrir la nouvelle consultation sur l'éducation") description: String,
        @RequestParam("type", defaultValue = "Type de la notification pour rediriger l'utilisateur vers la bonne page ALL_REPONSES_QAGS, HOME_QAGS, DETAILS_QAG, HOME_CONSULTATIONS, DETAILS_CONSULTATION, REPONSE_SUPPORT") type: TypeNotification,
        @RequestParam("routeArgument", defaultValue = "f55a68a7-8ec3-46c8-ba6b-1484fbd77ec6") pageArgument: String?,
    ): ResponseEntity<*> {
        val result = sendUsersNotificationUseCase.execute(
            title = title,
            description = description,
            pageArgument = pageArgument,
            typeNotification = type,
        )

        return when (result) {
            NotificationResult.SUCCESS -> ResponseEntity.ok().body(Unit)
            NotificationResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
        }
    }
}

enum class TypeNotification {
    ALL_REPONSES_QAGS,//  ???,
    HOME_QAGS,
    DETAILS_QAG,
    HOME_CONSULTATIONS,
    DETAILS_CONSULTATION,
    REPONSE_SUPPORT,
}
