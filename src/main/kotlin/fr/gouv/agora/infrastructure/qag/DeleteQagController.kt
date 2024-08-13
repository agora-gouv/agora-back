package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qag.*
import fr.gouv.agora.usecase.qag.repository.QagDeleteResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
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
