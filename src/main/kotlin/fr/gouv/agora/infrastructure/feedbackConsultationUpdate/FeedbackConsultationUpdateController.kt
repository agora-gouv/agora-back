package fr.gouv.agora.infrastructure.feedbackConsultationUpdate

import fr.gouv.agora.infrastructure.feedbackConsultationUpdate.FeedbackConsultationUpdateQueue.TaskType
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.feedbackConsultationUpdate.FeedbackConsultationUpdateUseCase
import fr.gouv.agora.usecase.feedbackConsultationUpdate.InsertFeedbackConsultationUpdateResults
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Consultations")
class FeedbackConsultationUpdateController(
    private val useCase: FeedbackConsultationUpdateUseCase,
    private val queue: FeedbackConsultationUpdateQueue,
    private val mapper: FeedbackConsultationUpdateJsonMapper,
) {

    @Operation(summary = "Post Consultation Feedback Update")
    @PostMapping("/consultations/{consultationId}/updates/{consultationUpdateId}/feedback")
    fun insertFeedbackConsultationUpdate(
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
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

    @Suppress("DeprecatedCallableAddReplaceWith")
    @DeleteMapping("/consultations/{consultationId}/updates/{consultationUpdateId}/feedback")
    @Deprecated("DELETE feedback is not needed anymore to change feedback using POST /consultations/{consultationId}/updates/{consultationUpdateId}/feedback, can be deleted once required app version is <TBD> (development not started yet)")
    fun deleteFeedbackConsultationUpdate(
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
        @PathVariable consultationId: String,
        @PathVariable consultationUpdateId: String,
    ): ResponseEntity<*> {
        return ResponseEntity.ok().body(Unit)
    }

}
