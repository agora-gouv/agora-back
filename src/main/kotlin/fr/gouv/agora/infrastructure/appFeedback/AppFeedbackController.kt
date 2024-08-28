package fr.gouv.agora.infrastructure.appFeedback

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.appFeedback.InsertAppFeedbackResult
import fr.gouv.agora.usecase.appFeedback.InsertAppFeedbackUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "App FeedBack")
class AppFeedbackController(
    private val insertAppFeedbackUseCase: InsertAppFeedbackUseCase,
    private val mapper: AppFeedbackJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {

    @PostMapping("/feedback")
    fun insertAppFeedback(
        @RequestBody body: AppFeedbackJson,
    ): HttpEntity<*> {
        return mapper.toDomain(
            json = body,
            userId = authentificationHelper.getUserId()!!,
        )?.let { appFeedback ->
            when (insertAppFeedbackUseCase.insertAppFeedback(appFeedback)) {
                InsertAppFeedbackResult.SUCCESS -> ResponseEntity.ok().body(Unit)
                InsertAppFeedbackResult.FAILURE -> ResponseEntity.badRequest().body(Unit)
            }
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}
