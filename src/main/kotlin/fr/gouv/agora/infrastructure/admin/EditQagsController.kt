package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.gouv.agora.usecase.qagSelection.UpdateWinningQagsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody as RequestBodySpring
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodySwagger

@RestController
@Tag(name = "Admin")
class EditQagsController(
    private val updateWinningQagsUseCase: UpdateWinningQagsUseCase,
) {

    @Operation(
        summary = "Mettre à jour les QAGs gagnantes",
        responses = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/admin/update_winning_qags")
    fun updateWinningQags(
        @RequestBodySpring
        @RequestBodySwagger(
            description = "Liste des IDs des QAG à promouvoir au statut SELECTED", required = true,
            content = [Content(
                schema = Schema(implementation = QagsIdListJson::class)
            )]
        ) body: QagsIdListJson,
    ): HttpEntity<*> {
        val results = updateWinningQagsUseCase.execute(body.qagsID)
        val numberOfUpdatedQags = results.count { it is QagUpdateResult.Success }

        return ResponseEntity.ok().body("$numberOfUpdatedQags QAGs mises à jour sur ${body.qagsID.size}")
    }
}