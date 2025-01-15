package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.consultation.ConsultationDetailsUpdateV2UseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Consultations")
class ConsultationDetailsUpdateV2Controller(
    private val useCase: ConsultationDetailsUpdateV2UseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {
    @Operation(summary = "Get Détails Consultation Update")
    @GetMapping(
        "/v2/consultations/{consultationIdOrSlug}/updates/{consultationUpdateIdOrSlug}",
        "/api/public/consultations/{consultationIdOrSlug}/updates/{consultationUpdateIdOrSlug}"
    )
    fun getConsultationDetailsUpdate(
        @PathVariable consultationIdOrSlug: String,
        @PathVariable consultationUpdateIdOrSlug: String,
    ): ResponseEntity<ConsultationDetailsV2Json> {
        val consultationDetails = useCase.getConsultationUnpublishedDetailsUpdate(consultationIdOrSlug, consultationUpdateIdOrSlug)

        return ResponseEntity.ok().body(mapper.toJson(consultationDetails))
    }
}
