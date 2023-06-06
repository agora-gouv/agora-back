package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.consultation.GetConsultationParticipantCountUseCase
import fr.social.gouv.agora.usecase.consultation.GetConsultationUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Suppress("unused")
class ConsultationController(
    private val getConsultationUseCase: GetConsultationUseCase,
    private val getConsultationParticipantCountUseCase: GetConsultationParticipantCountUseCase,
    private val consultationDetailsJsonMapper: ConsultationDetailsJsonMapper,
) {

    @GetMapping("/consultations/{consultationId}")
    fun getConsultationDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable consultationId: String,
    ): ResponseEntity<*> {
        return getConsultationUseCase.getConsultation(
            consultationId = consultationId,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )?.let { consultation ->
            ResponseEntity.ok()
                .body(
                    consultationDetailsJsonMapper.toJson(
                        domain = consultation,
                        participantCount = getConsultationParticipantCountUseCase.getCount(consultationId)
                    )
                )
        } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
    }

}