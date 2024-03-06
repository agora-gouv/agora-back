package fr.gouv.agora.infrastructure.consultationResponse

import fr.gouv.agora.infrastructure.consultationResponse.InsertResponseConsultationQueue.TaskType
import fr.gouv.agora.infrastructure.consultationResponse.repository.ConsultationResponseResultJsonCacheRepository
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.profile.AskForDemographicInfoUseCase
import fr.gouv.agora.usecase.consultationResponse.ControlResponseConsultationUseCase
import fr.gouv.agora.usecase.consultationResponse.InsertReponseConsultationUseCase
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class ReponseConsultationController(
    private val insertReponseConsultationUseCase: InsertReponseConsultationUseCase,
    private val controlResponseConsultationUseCase: ControlResponseConsultationUseCase,
    private val askForDemographicInfoUseCase: AskForDemographicInfoUseCase,
    private val cacheRepository: ConsultationResponseResultJsonCacheRepository,
    private val jsonMapper: ReponseConsultationJsonMapper,
    private val queue: InsertResponseConsultationQueue,
) {
    @PostMapping("/consultations/{consultationId}/responses")
    fun postResponseConsultation(
        @PathVariable consultationId: String,
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody responsesConsultationJson: ReponsesConsultationJson,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)

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
                            cacheRepository.evictConsultationResults(consultationId)
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