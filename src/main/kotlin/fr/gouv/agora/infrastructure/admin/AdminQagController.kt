package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.qag.AdminUpdateQagStatusResult
import fr.gouv.agora.usecase.qag.AdminUpdateQagStatusUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class AdminQagController(
    private val adminUpdateQagStatusUseCase: AdminUpdateQagStatusUseCase,
) {

    @Operation(
        summary = "Modifier arbitrairement le statut d'une question",
        description = "Met à jour le statut d'une QAG sans déclencher de notifications ni de post-traitements.",
        parameters = [
            Parameter(
                name = "qagId",
                description = "Identifiant UUID de la question",
                required = true,
            )
        ],
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nouveau statut à appliquer",
            required = true,
            content = [Content(schema = Schema(implementation = UpdateQagStatusJson::class))],
        ),
        responses = [
            ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès"),
            ApiResponse(responseCode = "404", description = "Question introuvable"),
            ApiResponse(responseCode = "500", description = "Erreur lors de la mise à jour"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
        ]
    )
    @PutMapping("/admin/qags/{qagId}/status")
    fun updateQagStatus(
        @PathVariable qagId: String,
        @RequestBody body: UpdateQagStatusJson,
    ): ResponseEntity<String> {
        return when (adminUpdateQagStatusUseCase.updateQagStatus(qagId = qagId, newStatus = body.status)) {
            AdminUpdateQagStatusResult.Success -> ResponseEntity.ok("Statut de la question $qagId mis à jour : ${body.status}")
            AdminUpdateQagStatusResult.NotFound -> ResponseEntity.notFound().build()
            AdminUpdateQagStatusResult.Failure -> ResponseEntity.internalServerError().body("Échec de la mise à jour du statut")
        }
    }
}
