package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultationPaginated.ConsultationsFinishedPaginatedListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Consultations")
class ConsultationFinishedPaginatedController(
    private val consultationsFinishedPaginatedListUseCase: ConsultationsFinishedPaginatedListUseCase,
    private val consultationPaginatedJsonMapper: ConsultationPaginatedJsonMapper,
) {

    @Operation(summary = "Get Consultations Terminées")
    @GetMapping("/consultations/finished/{pageNumber}")
    fun getConsultationFinishedList(
        @PathVariable pageNumber: String,
        @RequestParam territory: String?,
        // TODO à tester et peut être retourner des enum via la route référentiel ?
    ): ResponseEntity<ConsultationPaginatedJson> {
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            consultationsFinishedPaginatedListUseCase.getConsultationFinishedPaginatedList(pageNumberInt, territory)
        }?.let { consultationFinishedPaginatedList ->
            ResponseEntity.ok(consultationPaginatedJsonMapper.toJson(consultationFinishedPaginatedList))
        } ?: ResponseEntity.badRequest().build()
    }
}
