package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.usecase.consultationResponse.GetConsultationResultsForOpenQuestionOnlyUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class GetConsultationResponsesOpenQuestionOnlyController(
    private val consultationResultsUseCase: GetConsultationResultsForOpenQuestionOnlyUseCase,
    private val mapper: ConsultationResultOpenQuestionOnlyTsvMapper,
) {

    @GetMapping("/admin/consultations/{consultationId}/responses/open")
    fun getConsultationResults(@PathVariable consultationId: String): ResponseEntity<*> {
        return consultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            ResponseEntity.ok().body(mapper.buildTsvBody(consultationResult))
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}