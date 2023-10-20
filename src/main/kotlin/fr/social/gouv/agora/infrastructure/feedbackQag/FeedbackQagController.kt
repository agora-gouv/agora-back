package fr.social.gouv.agora.infrastructure.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.feedbackQag.FeedbackQagListResult
import fr.social.gouv.agora.usecase.feedbackQag.InsertFeedbackQagUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class FeedbackQagController(
    private val insertFeedbackQagUseCase: InsertFeedbackQagUseCase,
    private val queue: FeedbackQagQueue,
    private val mapper: FeedbackJsonMapper,
) {
    @PostMapping("/qags/{qagId}/feedback")
    fun insertFeedbackQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
        @RequestBody body: FeedbackQagJson,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
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
                    is FeedbackQagListResult.Success -> if (!insertResult.feedbackQagList.isNullOrEmpty())
                        ResponseEntity.ok().body(mapper.toJson(insertResult.feedbackQagList)) else ResponseEntity.ok().body("")

                    else -> ResponseEntity.badRequest().body("")
                }
            },
            onTaskRejected = { ResponseEntity.badRequest().body("") }
        )
    }
}