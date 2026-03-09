package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.qagArchive.ArchiveAllModeratedQagsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class ArchiveAllModeratedQagsController(
    private val archiveAllModeratedQagsUseCase: ArchiveAllModeratedQagsUseCase,
) {
    @Operation(
        summary = "Archive toutes les questions modérées acceptées",
        responses = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur"
            )
        ]
    )
    @PostMapping("/admin/archive_all_moderated_qags")
    fun archiveAllModeratedQags(): HttpEntity<*> {
        val result = archiveAllModeratedQagsUseCase.archiveAllModeratedQags()
        return ResponseEntity.ok().body("${result.archivedCount} questions archivées")
    }
}
