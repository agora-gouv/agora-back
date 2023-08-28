package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.usecase.moderatus.GetModeratusQagListUseCase
import fr.social.gouv.agora.usecase.moderatus.ModeratusLoginUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Suppress("unused")
class ModeratusGetQagController(
    private val moderatusLoginUseCase: ModeratusLoginUseCase,
    private val moderatusQagListUseCase: GetModeratusQagListUseCase,
    private val mapper: ModeratusQagListXmlMapper,
) {

    @GetMapping("/moderatus/new_qags")
    fun getQagToModerate(
        @RequestParam("password") loginToken: String,
        @RequestParam("mediaType") mediaType: String,
    ): ResponseEntity<*> {
        if (!moderatusLoginUseCase.login(loginToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ModeratusQagListErrorXml(errorMessage = "Password invalide: aucun compte associé ou utilisateur non autorisé"))
        }

        return ResponseEntity.ok(
            mapper.toXml(moderatusQagListUseCase.getQagModeratingList())
        )
    }

}