package fr.gouv.agora.infrastructure.reponseConsultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.reponseConsultation.GetConsultationResultsWithDemographicRatiosUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class GetConsultationResponsesWithDemographicInfoController(
    private val consultationResultsUseCase: GetConsultationResultsWithDemographicRatiosUseCase,
    private val mapper: ConsultationResultWithDemographicInfoTsvMapper,
) {

    @GetMapping("/admin/consultations/{consultationId}/responses")
    fun getConsultationResults(@PathVariable consultationId: String): ResponseEntity<*> {
        return consultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            ResponseEntity.ok().body(mapper.buildTsvBody(consultationResult))
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}