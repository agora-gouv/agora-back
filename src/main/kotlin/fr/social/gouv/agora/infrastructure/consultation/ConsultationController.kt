package fr.social.gouv.agora.infrastructure.consultation

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
    private val consultationDetailsJsonMapper: ConsultationDetailsJsonMapper,
) {

    @GetMapping("/consultations/{id}")
    fun getConsultationDetails(@PathVariable id: String): HttpEntity<*> {
        return getConsultationUseCase.getConsultation(id)?.let { consultation ->
            ResponseEntity.ok()
                .body(consultationDetailsJsonMapper.toJson(consultation))
        } ?: ResponseEntity.EMPTY
    }

}