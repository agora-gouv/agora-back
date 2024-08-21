package fr.gouv.agora.infrastructure.feedbackQag

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.FeedbackQagInserting
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.feedbackQag.InsertFeedbackQagResult
import fr.gouv.agora.usecase.feedbackQag.InsertFeedbackQagUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "QaG")
class FeedbackQagController(
    private val insertFeedbackQagUseCase: InsertFeedbackQagUseCase,
    private val queue: FeedbackQagQueue,
    private val mapper: FeedbackJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {
    @Operation(summary = "Post QaG Feedback")
    @PostMapping("/qags/{qagId}/feedback")
    fun insertFeedbackQag(
        @PathVariable qagId: String,
        @RequestBody body: FeedbackQagJson,
    ): HttpEntity<*> {
        val userId = authentificationHelper.getUserId()!!
        return queue.executeTask(
            taskType = FeedbackQagQueue.TaskType.AddFeedback(userId = userId),
            onTaskExecuted = {
                val insertResult = insertFeedbackQagUseCase.insertFeedbackQag(
                    FeedbackQagInserting(
                        qagId = qagId,
                        userId = userId,
                        isHelpful = body.isHelpful,
                    )
                )
                when (insertResult) {
                    is InsertFeedbackQagResult.Success -> ResponseEntity.ok()
                        .body(mapper.toJson(insertResult.feedbackResults))
                    InsertFeedbackQagResult.SuccessFeedbackDisabled -> ResponseEntity.ok().body(Unit)
                    else -> ResponseEntity.badRequest().body(Unit)
                }
            },
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) }
        )
    }
}
