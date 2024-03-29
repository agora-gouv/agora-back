package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qag.*
import fr.gouv.agora.usecase.qag.repository.QagDeleteResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class DeleteQagController(
    private val deleteQagUseCase: DeleteQagUseCase,
) {

    @DeleteMapping("/qags/{qagId}")
    fun deleteQagById(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        val qagDeleteResult = deleteQagUseCase.deleteQagById(
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
            qagId = qagId,
        )
        return when (qagDeleteResult) {
            is QagDeleteResult.Success -> ResponseEntity.ok().body(Unit)
            QagDeleteResult.Failure -> ResponseEntity.badRequest().body(Unit)
        }
    }
}