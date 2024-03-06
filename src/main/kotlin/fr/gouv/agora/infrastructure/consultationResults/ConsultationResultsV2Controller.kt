package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.usecase.consultationResults.ConsultationResultsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationResultsV2Controller(
    private val consultationResultsUseCase: ConsultationResultsUseCase,
    private val mapper: ConsultationResultJsonMapper,
) {

    @GetMapping("/v2/consultations/{consultationId}/responses")
    fun getConsultationResults(@PathVariable consultationId: String): ResponseEntity<*> {
        return consultationResultsUseCase.getConsultationResults(consultationId)?.let {results ->
            ResponseEntity.ok().body(mapper.toJson(results))
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}