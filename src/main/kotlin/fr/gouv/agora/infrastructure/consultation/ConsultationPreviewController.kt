package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultation.ConsultationPreviewUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationPreviewController(
    private val consultationPreviewUseCase: ConsultationPreviewUseCase,
    private val consultationPreviewJsonMapper: ConsultationPreviewJsonMapper,
) {
    @GetMapping("/consultations")
    fun getConsultationPreviewOngoingList(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<ConsultationPreviewJson> {
        val consultationPreviewPage = consultationPreviewUseCase.getConsultationPreviewPage(
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )

        return ResponseEntity.ok().body(
            consultationPreviewJsonMapper.toJson(
                domainOngoingList = consultationPreviewPage.ongoingList,
                domainFinishedList = consultationPreviewPage.finishedList,
                domainAnsweredList = consultationPreviewPage.answeredList,
            )
        )
    }
}
