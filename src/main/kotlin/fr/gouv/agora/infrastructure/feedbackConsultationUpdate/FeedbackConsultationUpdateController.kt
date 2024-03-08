package fr.gouv.agora.infrastructure.feedbackConsultationUpdate

import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.FeedbackConsultationUpdateQueue.TaskType
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.feedbackConsultationUpdate.FeedbackConsultationUpdateUseCase
import fr.gouv.agora.usecase.feedbackConsultationUpdate.InsertFeedbackConsultationUpdateResults
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class FeedbackConsultationUpdateController(
    private val useCase: FeedbackConsultationUpdateUseCase,
    private val queue: FeedbackConsultationUpdateQueue,
    private val mapper: FeedbackConsultationUpdateJsonMapper,
) {

    @PostMapping("/consultations/{consultationId}/updates/{consultationUpdateId}/feedback")
    fun insertFeedbackConsultationUpdate(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable consultationId: String,
        @PathVariable consultationUpdateId: String,
        @RequestBody body: InsertFeedbackConsultationUpdateJson,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return queue.executeTask(
            taskType = TaskType.AddFeedback(userId = userId),
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) },
            onTaskExecuted = {
                val feedbackInserting = mapper.toInserting(
                    json = body,
                    userId = userId,
                    consultationId = consultationId,
                    consultationUpdateId = consultationUpdateId,
                )
                when (val result = useCase.insertFeedback(feedbackInserting)) {
                    InsertFeedbackConsultationUpdateResults.Failure -> ResponseEntity.badRequest().body(Unit)
                    is InsertFeedbackConsultationUpdateResults.Success -> ResponseEntity.ok().body(
                        mapper.toJson(result.feedbackResults)
                    )
                }
            },
        )
    }

}