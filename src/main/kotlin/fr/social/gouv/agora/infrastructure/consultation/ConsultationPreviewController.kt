package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.consultation.GetConsultationPreviewAnsweredListUseCase
import fr.social.gouv.agora.usecase.consultation.GetConsultationPreviewFinishedListUseCase
import fr.social.gouv.agora.usecase.consultation.GetConsultationPreviewOngoingListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationPreviewController(
    private val getOngoingListUseCase: GetConsultationPreviewOngoingListUseCase,
    private val getFinishedListUseCase: GetConsultationPreviewFinishedListUseCase,
    private val getAnsweredListUseCase: GetConsultationPreviewAnsweredListUseCase,
    private val consultationPreviewJsonMapper: ConsultationPreviewJsonMapper,
) {
    @GetMapping("/consultations")
    fun getConsultationPreviewOngoingList(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<ConsultationPreviewJson> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val consultationListOngoing = getOngoingListUseCase.getConsultationPreviewOngoingList(userId = userId)
        val consultationListFinished = getFinishedListUseCase.getConsultationPreviewFinishedList()
        val consultationListAnswered = getAnsweredListUseCase.getConsultationPreviewAnsweredList(userId = userId)

        return ResponseEntity.ok().body(
            consultationPreviewJsonMapper.toJson(
                domainOngoingList = consultationListOngoing,
                domainFinishedList = consultationListFinished,
                domainAnsweredList = consultationListAnswered,
            )
        )
    }
}