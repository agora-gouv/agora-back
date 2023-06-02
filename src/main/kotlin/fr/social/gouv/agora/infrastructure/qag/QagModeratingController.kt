package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.GetQagModeratingListUseCase
import fr.social.gouv.agora.usecase.qag.PutQagModeratingUseCase
import fr.social.gouv.agora.usecase.qag.repository.ModeratingQagResult
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
    ): ResponseEntity<QagModeratingHomeJson> {
        val qagModeratingList = getQagModeratingListUseCase.getQagModeratingList(
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        val qagModeratingCount = getQagModeratingListUseCase.getModeratingQagCount()
        return ResponseEntity.ok(mapper.toJson(qagModeratingList, qagModeratingCount))
    }

    @PutMapping("/moderate/qags/{qagId}")
    fun putModeratingQagStatus(
        @PathVariable qagId: String,
        @RequestBody body: QagModeratingStatusJson,
    ): ResponseEntity<*> {
        return when (putQagModeratingUseCase.putModeratingQagStatus(qagId = qagId, body.isAccepted)) {
            ModeratingQagResult.FAILURE -> ResponseEntity.status(400).body("")
            ModeratingQagResult.SUCCESS -> ResponseEntity.ok().body("")
        }
    }
}