package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.usecase.consultationResponse.GetConsultationResultsWithDemographicRatiosUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Admin")
class GetConsultationResponsesWithDemographicInfoController(
    private val consultationResultsUseCase: GetConsultationResultsWithDemographicRatiosUseCase,
    private val mapper: ConsultationResultWithDemographicInfoTsvMapper,
) {

    @Operation(summary = "Get Consultations Responses Demographic Infos")
    @GetMapping("/admin/consultations/{consultationId}/responses")
    fun getConsultationResults(@PathVariable consultationId: String): ResponseEntity<*> {
        return consultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            ResponseEntity.ok().body(mapper.buildTsvBody(consultationResult))
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}
