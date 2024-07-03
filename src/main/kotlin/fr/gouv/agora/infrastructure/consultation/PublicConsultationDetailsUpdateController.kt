package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.usecase.consultation.PublicConsultationDetailsUpdateUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class PublicConsultationDetailsUpdateController(
    private val useCase: PublicConsultationDetailsUpdateUseCase,
    private val mapper: ConsultationDetailsV2JsonMapper,
) {

    @GetMapping("/api/public/consultations/{consultationId}/updates/{consultationUpdateId}")
    fun getConsultationDetailsUpdate(
        @PathVariable consultationId: String,
        @PathVariable consultationUpdateId: String,
    ): ResponseEntity<ConsultationDetailsV2Json> {
        return useCase.getConsultationUpdate(
            consultationId = consultationId,
            consultationUpdateId = consultationUpdateId,
        )?.let { consultationDetails ->
            ResponseEntity.ok().body(mapper.toJson(consultationDetails))
        } ?: ResponseEntity.notFound().build()
    }

}
