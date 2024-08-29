package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.usecase.consultationResults.ConsultationResultsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Consultations")
class ConsultationResultsV2Controller(
    private val consultationResultsUseCase: ConsultationResultsUseCase,
    private val mapper: ConsultationResultJsonMapper,
) {
    @Operation(summary = "Get Consultations Results")
    @GetMapping("/v2/consultations/{consultationId}/responses")
    fun getConsultationResults(@PathVariable consultationId: String): ResponseEntity<*> {
        return consultationResultsUseCase.getConsultationResults(consultationId)?.let { results ->
            ResponseEntity.ok().body(mapper.toJson(results))
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}
