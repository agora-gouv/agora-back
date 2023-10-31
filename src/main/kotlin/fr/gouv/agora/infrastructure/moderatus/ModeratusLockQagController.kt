package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.usecase.moderatus.LockModeratusQagListUseCase
import fr.gouv.agora.usecase.moderatus.ModeratusLoginResult
import fr.gouv.agora.usecase.moderatus.ModeratusLoginUseCase
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
        val lockedQagIdList = lockedQagIds
            .split(LOCKED_QAG_IDS_SEPARATOR)
            .filter { lockedQagId -> lockedQagId.isNotBlank() }
            .map { lockedQagId -> lockedQagId.trim() }

        if (moderatusLoginUseCase.login(loginToken) == ModeratusLoginResult.Failure) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                    mapper.toErrorXml(
                        lockedQagIds = lockedQagIdList,
                        errorMessage = "Password invalide",
                    )
                )
        }

        return ResponseEntity.ok(mapper.toXml(lockModeratusQagListUseCase.lockQagIds(lockedQagIdList)))
    }

}