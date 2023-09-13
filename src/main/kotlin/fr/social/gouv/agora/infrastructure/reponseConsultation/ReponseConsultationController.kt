package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.infrastructure.reponseConsultation.InsertResponseConsultationQueue.TaskType
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.profile.AskForDemographicInfoUseCase
import fr.social.gouv.agora.usecase.reponseConsultation.InsertReponseConsultationUseCase
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class ReponseConsultationController(
    private val insertReponseConsultationUseCase: InsertReponseConsultationUseCase,
    private val askForDemographicInfoUseCase: AskForDemographicInfoUseCase,
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
                val statusInsertion = insertReponseConsultationUseCase.insertReponseConsultation(
                    consultationId = consultationId,
                    userId = userId,
                    consultationResponses = jsonMapper.toDomain(responsesConsultationJson),
                )
                when (statusInsertion) {
                    InsertResult.INSERT_SUCCESS -> {
                        val askDemographicInfo = askForDemographicInfoUseCase.askForDemographicInfo(userId = userId)
                        ResponseEntity.ok()
                            .body(ResponseConsultationResultJson(askDemographicInfo = askDemographicInfo))
                    }
                    InsertResult.INSERT_FAILURE -> ResponseEntity.badRequest().body(Unit)
                }
            },
            onTaskRejected = { ResponseEntity.badRequest().body(Unit) },
        )
    }
}