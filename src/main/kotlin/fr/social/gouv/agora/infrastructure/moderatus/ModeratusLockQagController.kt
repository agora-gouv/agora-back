package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.usecase.moderatus.LockModeratusQagListUseCase
import fr.social.gouv.agora.usecase.moderatus.ModeratusLoginUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ModeratusLockQagController(
    private val moderatusLoginUseCase: ModeratusLoginUseCase,
    private val lockModeratusQagListUseCase: LockModeratusQagListUseCase,
    private val mapper: ModeratusQagLockResultXmlMapper,
) {

    private companion object {
        private const val LOCKED_QAG_IDS_SEPARATOR = ";"
    }

    @GetMapping("/moderatus/ack_qags")
    fun lockQag(
        @RequestParam("password") loginToken: String,
        @RequestParam("mediaType") mediaType: String,
        @RequestParam("content_id") lockedQagIds: String,
    ): ResponseEntity<*> {
        if (!moderatusLoginUseCase.login(loginToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ModeratusQagListErrorXml(errorMessage = "Password invalide: aucun compte associé ou utilisateur non autorisé"))
        }

        return ResponseEntity.ok(
            mapper.toXml(
                lockModeratusQagListUseCase.lockQagIds(
                    lockedQagIds
                        .split(LOCKED_QAG_IDS_SEPARATOR)
                        .filter { lockedQagId -> lockedQagId.isNotBlank() }
                        .map { lockedQagId -> lockedQagId.trim() }
                )
            )
        )
    }

}