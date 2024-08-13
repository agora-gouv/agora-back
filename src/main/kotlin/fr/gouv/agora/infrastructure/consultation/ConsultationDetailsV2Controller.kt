package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultation.ConsultationDetailsV2UseCase
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
class ConsultationDetailsV2Controller(
    private val getConsultationUseCase: ConsultationDetailsV2UseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
    private val authentificationHelper: AuthentificationHelper,
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
            userId = authentificationHelper.getUserId(),
        )?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }
}
