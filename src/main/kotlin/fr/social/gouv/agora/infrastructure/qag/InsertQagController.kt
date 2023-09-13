package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.infrastructure.qag.InsertQagQueue.TaskType
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.AskQagStatus
import fr.social.gouv.agora.usecase.qag.GetAskQagStatusUseCase
import fr.social.gouv.agora.usecase.qag.InsertQagUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class InsertQagController(
    private val insertQagUseCase: InsertQagUseCase,
    private val getAskQagStatusUseCase: GetAskQagStatusUseCase,
    private val mapper: QagJsonMapper,
    private val queue: InsertQagQueue,
) {

    @PostMapping("/qags")
    fun insertQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody qagJson: QagInsertingJson,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return queue.executeTask(
            taskType = TaskType.InsertQag(userId = userId),
            onTaskExecuted = {
                if (getAskQagStatusUseCase.getAskQagStatus(userId = userId) == AskQagStatus.ENABLED) {
                    when (val result = insertQagUseCase.insertQag(mapper.toDomain(json = qagJson, userId = userId))) {
                        QagInsertionResult.Failure -> ResponseEntity.badRequest().body(Unit)
                        is QagInsertionResult.Success -> ResponseEntity.ok().body(mapper.toJson(result))
                    }
                } else ResponseEntity.badRequest().body(Unit)
            },
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) },
        )
    }

}