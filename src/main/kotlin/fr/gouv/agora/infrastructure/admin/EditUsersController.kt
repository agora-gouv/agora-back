package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.changeUserAuthorizationLevel.DowngradeUsersUseCase
import fr.gouv.agora.usecase.changeUserAuthorizationLevel.UpgradeUsersToPublisherUseCase
import fr.gouv.agora.usecase.deleteUsers.DeleteUsersUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodySwagger
import org.springframework.web.bind.annotation.RequestBody as RequestBodySpring

@RestController
@Tag(name = "Admin")
class EditUsersController(
    private val deleteUsersUseCase: DeleteUsersUseCase,
    private val upgradeUsersToPublisherUseCase: UpgradeUsersToPublisherUseCase,
    private val downgradeUsersUseCase: DowngradeUsersUseCase,
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
        deleteUsersUseCase.deleteUsers(body.userIDsToDelete)
        return ResponseEntity.ok().body(Unit)
    }

    @Operation(
        summary = "Promouvoir des utilisateurs au rang de publisher",
        responses = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/admin/upgrade_users_to_publisher")
    fun upgradeUsersToPublisher(
        @RequestBodySpring
        @RequestBodySwagger(
            description = "Liste des IDs utilisateur à promouvoir au rôle de publisher", required = true,
            content = [Content(
                schema = Schema(implementation = UsersIdListJson::class)
            )]
        ) body: UsersIdListJson,
    ): HttpEntity<*> {
        val numberOfUpgradedUsers = upgradeUsersToPublisherUseCase.execute(body.usersID)

        return ResponseEntity.ok().body("$numberOfUpgradedUsers utilisateurs promus sur ${body.usersID.size}")
    }

    @Operation(
        summary = "Révoquer les droits d'utilisateurs",
        responses = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/admin/downgrade_users")
    fun downgradeUsers(
        @RequestBodySpring
        @RequestBodySwagger(
            description = "Liste des IDs utilisateur à révoquer", required = true,
            content = [Content(
                schema = Schema(implementation = UsersIdListJson::class)
            )]
        ) body: UsersIdListJson,
    ): HttpEntity<*> {
        val numberOfDowngradedUsers = downgradeUsersUseCase.execute(body.usersID)

        return ResponseEntity.ok().body("$numberOfDowngradedUsers utilisateurs révoqués sur ${body.usersID.size}")
    }
}
