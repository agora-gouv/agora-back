package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.feedbackQag.FeedbackQagUseCase
import fr.social.gouv.agora.usecase.feedbackQag.repository.GetFeedbackQagRepository
import fr.social.gouv.agora.usecase.qag.*
import fr.social.gouv.agora.usecase.supportQag.SupportQagUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagDetailsController(
    private val getQagUseCase: GetQagUseCase,
    private val supportQagUseCase: SupportQagUseCase,
    private val feedbackQagUseCase: FeedbackQagUseCase,
    private val mapper: QagJsonMapper,
) {

    @GetMapping("/qags/{qagId}")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        val userId =
        val qagResult = getQagUseCase.getQag(qagId = qagId, userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader) )
        return when (qagResult) {
            is QagResult.Success -> ResponseEntity.ok(
                mapper.toJson(
                    qagResult.qag,
                    userSupportedQagIds = supportQagUseCase.getUserSupportedQagIds(userId = userId),
                    userFeedbackQagIds = feedbackQagUseCase.getUserFeedbackQagIds(userId = userId),
                    userId = userId,
                )
            )
            QagResult.QagRejectedStatus -> ResponseEntity.status(HttpStatus.LOCKED).body(Unit)
            QagResult.QagNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Unit)
        }
    }

}