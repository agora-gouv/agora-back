package fr.social.gouv.agora.infrastructure.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.infrastructure.qag.QagJsonMapper
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.feedbackQag.InsertFeedbackQagUseCase
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import fr.social.gouv.agora.usecase.qag.GetQagUseCase
import fr.social.gouv.agora.usecase.qag.QagResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class FeedbackQagController(
    private val insertFeedbackQagUseCase: InsertFeedbackQagUseCase,
    private val queue: FeedbackQagQueue,
    private val getQagUseCase: GetQagUseCase,
    private val mapper: QagJsonMapper,
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
                val qagResult = getQagUseCase.getQag(
                    qagId = qagId,
                    userId = userId,
                )
                if (insertResult == FeedbackQagResult.SUCCESS && qagResult is QagResult.Success) {
                    ResponseEntity.ok().body(mapper.toJson(qagResult.qag, userId = userId).response?.feedbackResults)
                } else ResponseEntity.badRequest().body("")
            },
            onTaskRejected = { ResponseEntity.badRequest().body("") }
        )
    }
}