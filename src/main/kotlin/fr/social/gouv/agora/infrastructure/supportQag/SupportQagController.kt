package fr.social.gouv.agora.infrastructure.supportQag

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.infrastructure.supportQag.SupportQagQueue.TaskType
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.supportQag.DeleteSupportQagUseCase
import fr.social.gouv.agora.usecase.supportQag.InsertSupportQagUseCase
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class SupportQagController(
    private val insertSupportQagUseCase: InsertSupportQagUseCase,
    private val deleteSupportQagUseCase: DeleteSupportQagUseCase,
    private val queue: SupportQagQueue,
) {
    @PostMapping("/qags/{qagId}/support")
    fun insertSupportQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return queue.executeTask(
            taskType = TaskType.AddSupport(userId = userId),
            onTaskExecuted = {
                val insertResult = insertSupportQagUseCase.insertSupportQag(
                    SupportQagInserting(
                        qagId = qagId,
                        userId = userId,
                    )
                )
                if (insertResult == SupportQagResult.SUCCESS) {
                    ResponseEntity.ok().body(Unit)
                } else ResponseEntity.badRequest().body(Unit)
            },
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) }
        )
    }

    @DeleteMapping("/qags/{qagId}/support")
    fun deleteSupportQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return queue.executeTask(
            taskType = TaskType.RemoveSupport(userId = userId),
            onTaskExecuted = {
                val deleteResult = deleteSupportQagUseCase.deleteSupportQag(
                    SupportQagDeleting(
                        qagId = qagId,
                        userId = userId,
                    )
                )
                if (deleteResult == SupportQagResult.SUCCESS) {
                    ResponseEntity.ok().body(Unit)
                } else ResponseEntity.badRequest().body(Unit)
            },
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) }
        )
    }
}