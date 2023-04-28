package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.usecase.consultation.GetConsultationPreviewOngoingListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationPreviewController(
    private val getConsultationPreviewOngoingListUseCase: GetConsultationPreviewOngoingListUseCase,
    private val consultationPreviewJsonMapper: ConsultationPreviewJsonMapper,
) {
    @GetMapping("/consultations")
    fun getConsultationPreviewOngoingList(): ResponseEntity<ConsultationPreviewJson> {
        val consultationListOngoing = getConsultationPreviewOngoingListUseCase.getConsultationPreviewOngoingList()
        return ResponseEntity.ok()
            .body(consultationPreviewJsonMapper.toJson(consultationListOngoing ?: emptyList()))
    }
}