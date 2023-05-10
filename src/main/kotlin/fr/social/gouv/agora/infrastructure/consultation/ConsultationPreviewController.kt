package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.usecase.consultation.GetConsultationPreviewAnsweredListUseCase
import fr.social.gouv.agora.usecase.consultation.GetConsultationPreviewOngoingListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationPreviewController(
    private val getConsultationPreviewOngoingListUseCase: GetConsultationPreviewOngoingListUseCase,
    private val getConsultationPreviewAnsweredListUseCase: GetConsultationPreviewAnsweredListUseCase,
    private val consultationPreviewJsonMapper: ConsultationPreviewJsonMapper,
) {
    @GetMapping("/consultations")
    fun getConsultationPreviewOngoingList(@RequestHeader("deviceId") deviceId: String): ResponseEntity<ConsultationPreviewJson> {
        val consultationListOngoing = getConsultationPreviewOngoingListUseCase.getConsultationPreviewOngoingList()
        val consultationListAnswered =
            getConsultationPreviewAnsweredListUseCase.getConsultationPreviewAnsweredList(deviceId) ?: emptyList()
        return ResponseEntity.ok()
            .body(
                consultationPreviewJsonMapper.toJson(
                    domainOngoingList = consultationListOngoing,
                    domainAnsweredList = consultationListAnswered,
                )
            )
    }
}