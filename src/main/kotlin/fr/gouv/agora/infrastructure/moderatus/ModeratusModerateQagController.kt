package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.usecase.moderatus.ModerateModeratusQagUseCase
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
@Tag(name = "Moderatus")
class ModeratusModerateQagController(
    private val moderatusLoginUseCase: ModeratusLoginUseCase,
    private val moderateQagUseCase: ModerateModeratusQagUseCase,
    private val optionsMapper: ModerateQagOptionsMapper,
    private val mapper: ModeratusQagModerateResultPageXmlMapper,
) {

    @Operation(summary = "Get Moderate QaG")
    @GetMapping("/moderatus/result_qag")
    fun lockQag(
        @RequestParam("password") loginToken: String,
        @RequestParam("mediaType") mediaType: String,
        @RequestParam("content_id") qagId: String,
        @RequestParam("status") status: String,
        @RequestParam("motif") reason: String?,
        @RequestParam("kill") shouldDeleteFlag: String?,
    ): ResponseEntity<*> {
        val userId = when (val loginResult = moderatusLoginUseCase.login(loginToken)) {
            is ModeratusLoginResult.Success -> loginResult.userId
            ModeratusLoginResult.Failure -> return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapper.toErrorXml(errorMessage = "Password invalide: aucun compte associé ou utilisateur non autorisé"))
        }

        val moderateQagOptions = when (val result = optionsMapper.toModerateQagOptions(
            qagId = qagId,
            userId = userId,
            status = status,
            reason = reason,
            shouldDeleteFlag = shouldDeleteFlag?.toIntOrNull() ?: 0,
        )) {
            is ModerateQagOptionsMapper.Result.Success -> result.options
            is ModerateQagOptionsMapper.Result.Error -> return ResponseEntity.badRequest()
                .body(mapper.toErrorXml(errorMessage = result.errorMessage))
        }

        return ResponseEntity.ok(
            mapper.toXml(
                result = moderateQagUseCase.moderateQag(moderateQagOptions),
                errorMessage = "Error lors de la modification en base de données",
            )
        )
    }

}
