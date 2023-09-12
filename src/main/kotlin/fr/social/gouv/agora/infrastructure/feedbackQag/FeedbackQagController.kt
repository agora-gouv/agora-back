package fr.social.gouv.agora.infrastructure.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.feedbackQag.InsertFeedbackQagUseCase
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class FeedbackQagController(private val insertFeedbackQagUseCase: InsertFeedbackQagUseCase) {

    @PostMapping("/qags/{qagId}/feedback")
    fun insertFeedbackQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
        @RequestBody body: FeedbackQagJson,
    ): HttpEntity<*> {
        val insertResult = insertFeedbackQagUseCase.insertFeedbackQag(
            FeedbackQagInserting(
                qagId = qagId,
                userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
                isHelpful = body.isHelpful,
            )
        )
        return if (insertResult == FeedbackQagResult.SUCCESS) {
            ResponseEntity.ok().body("")
        } else ResponseEntity.status(400).body("")
    }
}