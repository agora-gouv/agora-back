package fr.social.gouv.agora.infrastructure.qagModerating

import fr.social.gouv.agora.usecase.qagModerating.GetQagModeratingListUseCase
import fr.social.gouv.agora.usecase.qagModerating.PutQagModeratingUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagModeratingController(
    private val getQagModeratingListUseCase: GetQagModeratingListUseCase,
    private val putQagModeratingUseCase: PutQagModeratingUseCase,
    private val mapper: QagModeratingJsonMapper,
) {

    @GetMapping("/moderate/qags")
    fun getQagToModerate(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<*> {
        return ResponseEntity.badRequest().body(Unit)
    }

    @PutMapping("/moderate/qags/{qagId}")
    fun putModeratingQagStatus(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
        @RequestBody body: QagModeratingStatusJson,
    ): ResponseEntity<*> {
        return ResponseEntity.badRequest().body(Unit)
    }
}
