package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.usecase.consultation.PublicConsultationDetailsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class PublicConsultationDetailsController(
    private val useCase: PublicConsultationDetailsUseCase,
    private val mapper: PublicConsultationDetailsJsonMapper,
) {

    @GetMapping("/api/public/consultations/{consultationId}")
    fun getConsultationDetails(
        @PathVariable consultationId: String,
    ): ResponseEntity<*> {
        return useCase.getConsultation(consultationId = consultationId)?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build<Unit>()
    }

}
