package fr.social.gouv.agora.infrastructure.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.usecase.feedbackQag.InsertFeedbackQagUseCase
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class FeedbackQagController(private val insertFeedbackQagUseCase: InsertFeedbackQagUseCase) {

    @PostMapping("/qags/{qagId}/feedback")
    fun insertFeedbackQag(
        @RequestHeader("deviceId") deviceId: String,
        @PathVariable qagId: String,
        @RequestBody body: FeedbackQagJson,
    ): HttpEntity<*> {
        val insertResult = insertFeedbackQagUseCase.insertFeedbackQag(
            FeedbackQagInserting(
                userId = deviceId,
                qagId = qagId,
                isHelpful = body.isHelpful,
            )
        )
        return if (insertResult == FeedbackQagResult.SUCCESS) {
            ResponseEntity.status(200).body("")
        } else ResponseEntity.status(400).body("")
    }
}