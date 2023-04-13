package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.usecase.consultation.GetConsultationParticipantCountUseCase
import fr.social.gouv.agora.usecase.consultation.GetConsultationUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    fun getConsultationDetails(@PathVariable consultationId: String): HttpEntity<*> {
        return getConsultationUseCase.getConsultation(consultationId)?.let { consultation ->
            ResponseEntity.ok()
                .body(
                    consultationDetailsJsonMapper.toJson(
                        domain = consultation,
                        participantCount = getConsultationParticipantCountUseCase.getCount(consultationId)
                    )
                )
        } ?: ResponseEntity.EMPTY
    }

}