package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.usecase.moderatus.GetModeratusQagListUseCase
import fr.gouv.agora.usecase.moderatus.ModeratusLoginResult
import fr.gouv.agora.usecase.moderatus.ModeratusLoginUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Moderatus")
class ModeratusGetQagController(
    private val moderatusLoginUseCase: ModeratusLoginUseCase,
    private val moderatusQagListUseCase: GetModeratusQagListUseCase,
    private val mapper: ModeratusQagListXmlMapper,
) {

    @Operation(summary = "Get New QaG")
    @GetMapping("/moderatus/new_qags")
    fun getQagToModerate(
        @RequestParam("password") loginToken: String,
        @RequestParam("mediaType") mediaType: String,
    ): ResponseEntity<*> {
        if (moderatusLoginUseCase.login(loginToken) == ModeratusLoginResult.Failure) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ModeratusQagListErrorXml(errorMessage = "Password invalide: aucun compte associé ou utilisateur non autorisé"))
        }

        return ResponseEntity.ok(
            mapper.toXml(moderatusQagListUseCase.getQagModeratingList())
        )
    }

}
