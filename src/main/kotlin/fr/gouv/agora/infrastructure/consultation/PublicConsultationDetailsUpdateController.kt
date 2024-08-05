package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.usecase.consultation.PublicConsultationDetailsUpdateUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Public")
class PublicConsultationDetailsUpdateController(
    private val useCase: PublicConsultationDetailsUpdateUseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {
    @Operation(summary = "Get Consultation Détails Update")
    @GetMapping("/api/public/consultations/{slugConsultation}/updates/{slugUpdate}")
    fun getConsultationDetailsUpdate(
        @PathVariable slugConsultation: String,
        @PathVariable slugUpdate: String,
    ): ResponseEntity<ConsultationDetailsV2Json> {
        return useCase.getConsultationUpdate(slugConsultation, slugUpdate)?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }
}
