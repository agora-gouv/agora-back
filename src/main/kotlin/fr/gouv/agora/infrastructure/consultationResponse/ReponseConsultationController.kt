package fr.gouv.agora.infrastructure.consultationResponse

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.infrastructure.consultationResponse.InsertResponseConsultationQueue.TaskType
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultationResponse.ControlResponseConsultationUseCase
import fr.gouv.agora.usecase.consultationResponse.InsertReponseConsultationUseCase
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertResult
import fr.gouv.agora.usecase.profile.AskForDemographicInfoUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Consultations")
class ReponseConsultationController(
    private val insertReponseConsultationUseCase: InsertReponseConsultationUseCase,
    private val controlResponseConsultationUseCase: ControlResponseConsultationUseCase,
    private val askForDemographicInfoUseCase: AskForDemographicInfoUseCase,
    private val jsonMapper: ReponseConsultationJsonMapper,
    private val queue: InsertResponseConsultationQueue,
    private val authentificationHelper: AuthentificationHelper,
) {
    @Operation(summary = "Post Consultation Responses")
    @PostMapping("/consultations/{consultationId}/responses")
    fun postResponseConsultation(
        @PathVariable consultationId: String,
        @RequestBody responsesConsultationJson: ReponsesConsultationJson,
    ): HttpEntity<*> {
        val userId = authentificationHelper.getUserId()!!
        return queue.executeTask(
            taskType = TaskType.InsertResponse(userId = userId),
            onTaskExecuted = {
                val consultationResponses = jsonMapper.toDomain(responsesConsultationJson)
                val isConsultationResponseValid = controlResponseConsultationUseCase.isResponseConsultationValid(
                    consultationId = consultationId,
                    consultationResponses = consultationResponses
                )
                if (!isConsultationResponseValid) ResponseEntity.badRequest().body(Unit)
                else {
                    val statusInsertion = insertReponseConsultationUseCase.insertReponseConsultation(
                        consultationId = consultationId,
                        userId = userId,
                        consultationResponses = consultationResponses,
                    )
                    when (statusInsertion) {
                        InsertResult.INSERT_SUCCESS -> {
                            val askDemographicInfo =
                                askForDemographicInfoUseCase.askForDemographicInfo(userId = userId)
                            ResponseEntity.ok()
                                .body(ResponseConsultationResultJson(askDemographicInfo = askDemographicInfo))
                        }

                        InsertResult.INSERT_FAILURE -> ResponseEntity.badRequest().body(Unit)
                    }
                }
            },
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) }
        )
    }
}
