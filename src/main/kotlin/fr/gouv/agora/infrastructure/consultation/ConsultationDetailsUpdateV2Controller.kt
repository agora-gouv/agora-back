package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultation.ConsultationDetailsUpdateV2UseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Consultations")
class ConsultationDetailsUpdateV2Controller(
    private val useCase: ConsultationDetailsUpdateV2UseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
    private val authentificationHelper: AuthentificationHelper,
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
        return useCase.getConsultationDetailsUpdate(
            consultationIdOrSlug = consultationIdOrSlug,
            consultationUpdateIdOrSlug = consultationUpdateIdOrSlug,
            userId = authentificationHelper.getUserId(),
        )?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }

}
