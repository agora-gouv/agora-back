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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "QaG")
class SupportQagController(
    private val insertSupportQagUseCase: InsertSupportQagUseCase,
    private val deleteSupportQagUseCase: DeleteSupportQagUseCase,
    private val isSuspiciousUserUseCase: IsSuspiciousUserUseCase,
    private val queue: SupportQagQueue,
) {
    @Operation(summary = "Post QaG Support")
    @PostMapping("/qags/{qagId}/support")
    fun insertSupportQag(
        @RequestHeader("User-Agent") userAgent: String,
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
        @PathVariable qagId: String,
        request: HttpServletRequest,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return queue.executeTask(
            taskType = TaskType.AddSupport(userId = userId),
            onTaskExecuted = {
                if (isSuspiciousUserUseCase.isSuspiciousActivity(
                        ipAddressHash = IpAddressUtils.retrieveIpAddressHash(request),
                        userAgent = userAgent,
                    )
                ) {
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

    @Operation(summary = "Delete QaG Support")
    @DeleteMapping("/qags/{qagId}/support")
    fun deleteSupportQag(
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
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
