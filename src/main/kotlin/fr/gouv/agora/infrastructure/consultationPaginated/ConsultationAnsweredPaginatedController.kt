package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.consultationPaginated.ConsultationsAnsweredPaginatedListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationAnsweredPaginatedController(
    private val consultationsAnsweredPaginatedListUseCase: ConsultationsAnsweredPaginatedListUseCase,
    private val consultationPaginatedJsonMapper: ConsultationPaginatedJsonMapper,
) {

    @GetMapping("/consultations/answered/{pageNumber}")
    fun getConsultationFinishedList(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            consultationsAnsweredPaginatedListUseCase.getConsultationAnsweredPaginatedList(
                userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
                pageNumber = pageNumberInt,
            )
        }?.let { consultationAnsweredPaginatedList ->
            ResponseEntity.ok(consultationPaginatedJsonMapper.toJson(consultationAnsweredPaginatedList))
        } ?: ResponseEntity.badRequest().body(Unit)
    }
}
