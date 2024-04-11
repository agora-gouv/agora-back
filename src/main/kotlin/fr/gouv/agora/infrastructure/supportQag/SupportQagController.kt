package fr.gouv.agora.infrastructure.supportQag

import fr.gouv.agora.domain.SupportQagDeleting
import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.infrastructure.supportQag.SupportQagQueue.TaskType
import fr.gouv.agora.infrastructure.utils.IpAddressUtils
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.supportQag.DeleteSupportQagUseCase
import fr.gouv.agora.usecase.supportQag.InsertSupportQagUseCase
import fr.gouv.agora.usecase.supportQag.repository.SupportQagResult
import fr.gouv.agora.usecase.suspiciousUser.IsSuspiciousUserUseCase
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class SupportQagController(
    private val insertSupportQagUseCase: InsertSupportQagUseCase,
    private val deleteSupportQagUseCase: DeleteSupportQagUseCase,
    private val isSuspiciousUserUseCase: IsSuspiciousUserUseCase,
    private val queue: SupportQagQueue,
) {
    @PostMapping("/qags/{qagId}/support")
    fun insertSupportQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
        request: HttpServletRequest,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return queue.executeTask(
            taskType = TaskType.AddSupport(userId = userId),
            onTaskExecuted = {
                if (isSuspiciousUserUseCase.isSuspiciousUser(IpAddressUtils.retrieveIpAddressHash(request))) {
                    ResponseEntity.ok().body(Unit)
                } else {
                    val insertResult = insertSupportQagUseCase.insertSupportQag(
                        SupportQagInserting(
                            qagId = qagId,
                            userId = userId,
                        )
                    )
                    if (insertResult == SupportQagResult.SUCCESS) {
                        ResponseEntity.ok().body(Unit)
                    } else ResponseEntity.badRequest().body(Unit)
                }
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