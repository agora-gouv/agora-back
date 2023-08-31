package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.usecase.moderatus.ModerateModeratusQagUseCase
import fr.social.gouv.agora.usecase.moderatus.ModeratusLoginResult
import fr.social.gouv.agora.usecase.moderatus.ModeratusLoginUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ModeratusModerateQagController(
    private val moderatusLoginUseCase: ModeratusLoginUseCase,
    private val moderateQagUseCase: ModerateModeratusQagUseCase,
    private val mapper: ModeratusQagModerateResultPageXmlMapper,
) {

    @GetMapping("/moderatus/result_qag")
    fun lockQag(
        @RequestParam("password") loginToken: String,
        @RequestParam("mediaType") mediaType: String,
        @RequestParam("content_id") qagId: String,
        @RequestParam("status") status: String,
    ): ResponseEntity<*> {
        val userId = when (val loginResult = moderatusLoginUseCase.login(loginToken)) {
            is ModeratusLoginResult.Success -> loginResult.userId
            ModeratusLoginResult.Failure -> return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapper.toErrorXml(errorMessage = "Password invalide: aucun compte associé ou utilisateur non autorisé"))
        }

        val isAccepted = when (status) {
            "OK" -> true
            "NOK" -> false
            else -> return ResponseEntity.badRequest().body(mapper.toErrorXml(errorMessage = "Status invalide"))
        }

        return ResponseEntity.ok(
            mapper.toXml(
                result = moderateQagUseCase.moderateQag(qagId = qagId, isAccepted = isAccepted, userId = userId),
                errorMessage = "Error lors de la modification en base de données",
            )
        )
    }

}