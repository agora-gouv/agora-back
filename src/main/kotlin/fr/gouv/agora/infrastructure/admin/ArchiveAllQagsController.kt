package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.qagArchive.ArchiveAllQagsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class ArchiveAllQagsController(
    private val archiveAllQagsUseCase: ArchiveAllQagsUseCase,
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
    @PostMapping("/admin/archive_all_qags")
    fun archiveAllQags(): ResponseEntity<ArchiveQagsResponse> {
        val result = archiveAllQagsUseCase.execute()
        return ResponseEntity.ok(
            ArchiveQagsResponse(
                archivedCount = result.archivedCount,
                message = "${result.archivedCount} questions archivées"
            )
        )
    }
}

data class ArchiveQagsResponse(
    val archivedCount: Int,
    val message: String,
)