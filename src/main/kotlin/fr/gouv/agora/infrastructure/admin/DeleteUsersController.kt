package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.deleteUsers.DeleteUsersUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodySwagger
import org.springframework.web.bind.annotation.RequestBody as RequestBodySpring

@RestController
@Suppress("unused")
@Tag(name = "Admin")
class DeleteUsersController(
    private val deleteUsersController: DeleteUsersUseCase,
) {
    @Operation(
        summary = "Supprimer un utilisateur",
        responses = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @DeleteMapping("/admin/delete_users")
    fun deleteUsers(
        @RequestBodySpring
        @RequestBodySwagger(
            description = "Liste des IDs utilisateur à supprimer", required = true,
            content = [Content(
                schema = Schema(implementation = DeleteUsersJson::class)
            )]
        ) body: DeleteUsersJson,
    ): HttpEntity<*> {
        deleteUsersController.deleteUsers(body.userIDsToDelete)
        return ResponseEntity.ok().body(Unit)
    }

}
