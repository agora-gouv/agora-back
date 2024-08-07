package fr.gouv.agora.infrastructure.appFeedback

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.appFeedback.InsertAppFeedbackResult
import fr.gouv.agora.usecase.appFeedback.InsertAppFeedbackUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "App FeedBack")
class AppFeedbackController(
    private val insertAppFeedbackUseCase: InsertAppFeedbackUseCase,
    private val mapper: AppFeedbackJsonMapper,
) {

    @PostMapping("/feedback")
    fun insertAppFeedback(
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
        @RequestBody body: AppFeedbackJson,
    ): HttpEntity<*> {
        return mapper.toDomain(
            json = body,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )?.let { appFeedback ->
            when (insertAppFeedbackUseCase.insertAppFeedback(appFeedback)) {
                InsertAppFeedbackResult.SUCCESS -> ResponseEntity.ok().body(Unit)
                InsertAppFeedbackResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
            }
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}
