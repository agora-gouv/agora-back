package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.usecase.consultation.PublicConsultationDetailsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Public")
class PublicConsultationDetailsController(
    private val useCase: PublicConsultationDetailsUseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {

    @Operation(summary = "Get Consultation Détails")
    @GetMapping("/api/public/consultations/{slugOrIdConsultation}")
    fun getConsultationDetails(
        @PathVariable slugOrIdConsultation: String,
    ): ResponseEntity<ConsultationDetailsV2Json> {
        return useCase.getConsultation(slugOrIdConsultation)?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }

}
