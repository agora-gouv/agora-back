package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.consultation.ConsultationDetailsV2UseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Consultations")
class ConsultationDetailsV2Controller(
    private val getConsultationUseCase: ConsultationDetailsV2UseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {
    @Operation(summary = "Get Détails Consultation")
    @GetMapping(
        "/v2/consultations/{consultationIdOrSlug}",
        "/api/public/consultations/{consultationIdOrSlug}"
    )
    fun getConsultationDetails(
        @PathVariable consultationIdOrSlug: String,
    ): ResponseEntity<ConsultationDetailsV2Json> {
        return getConsultationUseCase.getConsultation(
            consultationIdOrSlug = consultationIdOrSlug,
        )?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }
}
