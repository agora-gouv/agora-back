package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultation.ConsultationDetailsV2UseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationDetailsV2Controller(
    private val useCase: ConsultationDetailsV2UseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {

    @GetMapping("/v2/consultations/{consultationId}")
    fun getConsultationDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable consultationId: String,
    ): ResponseEntity<*> {
        return useCase.getConsultation(
            consultationId = consultationId,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build<Unit>()
    }

}