package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qag.GetQagModeratingListUseCase
import fr.social.gouv.agora.usecase.qag.ModeratingQagResult
import fr.social.gouv.agora.usecase.qag.PutQagModeratingUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagModeratingController(
    private val getQagModeratingListUseCase: GetQagModeratingListUseCase,
    private val putQagModeratingUseCase: PutQagModeratingUseCase,
    private val sendNotificationQagModeratedUseCase: SendNotificationQagModeratedUseCase,
    private val mapper: QagModeratingJsonMapper,
) {

    @GetMapping("/moderate/qags")
    fun getQagToModerate(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<QagModeratingHomeJson> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val qagModeratingList = getQagModeratingListUseCase.getQagModeratingList(userId)
        val qagModeratingCount = getQagModeratingListUseCase.getModeratingQagCount(userId)
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
