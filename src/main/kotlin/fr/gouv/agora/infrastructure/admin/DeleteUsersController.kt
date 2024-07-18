package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.deleteUsers.DeleteUsersUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Admin")
class DeleteUsersController(
    private val deleteUsersController: DeleteUsersUseCase,
) {
    @Operation(summary = "Admin Delete Users")
    @DeleteMapping("/admin/delete_users")
    fun deleteUsers(
        @RequestBody body: DeleteUsersJson,
    ): HttpEntity<*> {
        deleteUsersController.deleteUsers(body.userIDsToDelete)
        return ResponseEntity.ok().body(Unit)
    }

}
