package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.usecase.reponseConsultation.GetConsultationResultsUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class GetConsultationResponsesController(
    private val getConsultationResultsUseCase: GetConsultationResultsUseCase,
    private val consultationResultJsonMapper: ConsultationResultJsonMapper,
) {

    @GetMapping("/consultations/{consultationId}/responses")
    fun getConsultationResults(@PathVariable consultationId: String): HttpEntity<*> {
        return getConsultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            ResponseEntity.ok().body(consultationResultJsonMapper.toJson(consultationResult))
        } ?: ResponseEntity.EMPTY
    }

}