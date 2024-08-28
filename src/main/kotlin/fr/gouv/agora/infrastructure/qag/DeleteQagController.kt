package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.qag.DeleteQagUseCase
import fr.gouv.agora.usecase.qag.repository.QagDeleteResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "QaG")
class DeleteQagController(
    private val deleteQagUseCase: DeleteQagUseCase,
    private val authentificationHelper: AuthentificationHelper,
) {
    @Operation(summary = "Delete QaG")
    @DeleteMapping("/qags/{qagId}")
    fun deleteQagById(
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        val qagDeleteResult = deleteQagUseCase.deleteQagById(
            userId = authentificationHelper.getUserId()!!,
            qagId = qagId,
        )
        return when (qagDeleteResult) {
            is QagDeleteResult.Success -> ResponseEntity.ok().body(Unit)
            QagDeleteResult.Failure -> ResponseEntity.badRequest().body(Unit)
        }
    }
}
