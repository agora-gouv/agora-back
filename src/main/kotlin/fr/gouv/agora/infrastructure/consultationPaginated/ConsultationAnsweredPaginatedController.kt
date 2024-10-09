package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationsAnsweredPaginatedListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Consultations")
class ConsultationAnsweredPaginatedController(
    private val consultationsAnsweredPaginatedListUseCase: ConsultationsAnsweredPaginatedListUseCase,
    private val consultationPaginatedJsonMapper: ConsultationPaginatedJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {

    @Operation(summary = "Get Consultations Répondues")
    @GetMapping("/consultations/answered/{pageNumber}")
    fun getConsultationAnsweredList(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            consultationsAnsweredPaginatedListUseCase.getConsultationAnsweredPaginatedList(
                userId = authentificationHelper.getUserId()!!,
                pageNumber = pageNumberInt,
            )
        }?.let { consultationAnsweredPaginatedList ->
            ResponseEntity.ok(consultationPaginatedJsonMapper.toJson(consultationAnsweredPaginatedList))
        } ?: ResponseEntity.badRequest().body(Unit)
    }
}
