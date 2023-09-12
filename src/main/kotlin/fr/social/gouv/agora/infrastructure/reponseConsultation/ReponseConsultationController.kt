package fr.social.gouv.agora.infrastructure.reponseConsultation

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
    private val jsonMapper: ReponseConsultationJsonMapper
) {
    @PostMapping("/consultations/{consultationId}/responses")
    fun postResponseConsultation(
        @PathVariable consultationId: String,
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody responsesConsultationJson: ReponsesConsultationJson,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val statusInsertion = insertReponseConsultationUseCase.insertReponseConsultation(
            consultationId = consultationId,
            userId = userId,
            consultationResponses = jsonMapper.toDomain(responsesConsultationJson)
        )
        return when (statusInsertion) {
            InsertResult.INSERT_SUCCESS -> {
                val askDemographicInfo = askForDemographicInfoUseCase.askForDemographicInfo(userId = userId)
                ResponseEntity.ok()
                    .body(ResponseConsultationResultJson(askDemographicInfo = askDemographicInfo))
            }
            InsertResult.INSERT_FAILURE -> ResponseEntity.status(400).body("")
        }
    }
}