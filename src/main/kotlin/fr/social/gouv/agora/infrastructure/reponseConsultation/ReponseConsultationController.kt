package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.usecase.reponseConsultation.InsertReponseConsultationUseCase
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")

class ReponseConsultationController(
    private val insertReponseConsultationUseCase: InsertReponseConsultationUseCase,
    private val jsonMapper: ReponseConsultationJsonMapper
) {
    @PostMapping("/consultations/{consultationId}/responses")
    fun postResponseConsultation(
        @PathVariable consultationId: String,
        @RequestHeader deviceId: String,
        @RequestBody responsesConsultationJson: ReponsesConsultationJson,
    ): HttpEntity<*> {
        val statusInsertion = insertReponseConsultationUseCase.insertReponseConsultation(
            consultationId = consultationId,
            deviceId = deviceId,
            consultationResponses = jsonMapper.toDomain(responsesConsultationJson)
        )
        return when (statusInsertion) {
            InsertResult.INSERT_SUCCESS -> ResponseEntity.ok().body("")
            InsertResult.INSERT_FAILURE -> ResponseEntity.status(400).body("")
        }
    }
}