package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultation.ConsultationDetailsUpdateV2UseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationDetailsUpdateV2Controller(
    private val useCase: ConsultationDetailsUpdateV2UseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {

    @GetMapping("/v2/consultations/{consultationId}/updates/{consultationUpdateId}")
    fun getConsultationDetailsUpdate(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable consultationId: String,
        @PathVariable consultationUpdateId: String,
    ): ResponseEntity<ConsultationDetailsV2Json> {
        return useCase.getConsultationDetailsUpdate(
            consultationId = consultationId,
            consultationUpdateId = consultationUpdateId,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }

}
