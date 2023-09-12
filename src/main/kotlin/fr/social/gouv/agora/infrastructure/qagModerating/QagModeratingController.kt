package fr.social.gouv.agora.infrastructure.qagModerating

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qagModerating.GetQagModeratingListUseCase
import fr.social.gouv.agora.usecase.qagModerating.ModeratingQagResult
import fr.social.gouv.agora.usecase.qagModerating.PutQagModeratingUseCase
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
        val qagModeratingList = getQagModeratingListUseCase.getQagModeratingList(
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        val qagModeratingCount = getQagModeratingListUseCase.getModeratingQagCount()
        return ResponseEntity.ok(mapper.toJson(qagModeratingList, qagModeratingCount))
    }

    @PutMapping("/moderate/qags/{qagId}")
    fun putModeratingQagStatus(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
        @RequestBody body: QagModeratingStatusJson,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return when (putQagModeratingUseCase.putModeratingQagStatus(
            qagId = qagId,
            qagModeratingStatus = body.isAccepted,
            userId = userId,
        )) {
            ModeratingQagResult.FAILURE -> ResponseEntity.status(400).body("")
            ModeratingQagResult.SUCCESS -> ResponseEntity.ok().body("")
        }
    }
}
