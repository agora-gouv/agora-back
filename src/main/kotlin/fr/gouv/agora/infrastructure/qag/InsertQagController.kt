package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.infrastructure.qag.InsertQagQueue.TaskType
import fr.gouv.agora.usecase.qag.AskQagStatus
import fr.gouv.agora.usecase.qag.GetAskQagStatusUseCase
import fr.gouv.agora.usecase.qag.InsertQagUseCase
import fr.gouv.agora.usecase.qag.repository.QagInsertionResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "QaG")
class InsertQagController(
    private val insertQagUseCase: InsertQagUseCase,
    private val getAskQagStatusUseCase: GetAskQagStatusUseCase,
    private val mapper: QagJsonMapper,
    private val queue: InsertQagQueue,
    private val authentificationHelper: AuthentificationHelper,
) {

    @Operation(summary = "Post QaG")
    @PostMapping("/qags")
    fun insertQag(
        @RequestBody qagJson: QagInsertingJson,
    ): ResponseEntity<*> {
        val userId = authentificationHelper.getUserId()!!
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
