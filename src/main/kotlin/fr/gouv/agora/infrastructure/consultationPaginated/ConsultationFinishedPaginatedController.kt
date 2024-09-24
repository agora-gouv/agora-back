package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.usecase.consultationPaginated.ConsultationsByUserPreferencesUseCase
import fr.gouv.agora.usecase.consultationPaginated.ConsultationsFinishedPaginatedListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Consultations")
class ConsultationFinishedPaginatedController(
    private val consultationsFinishedByTerritoryUseCase: ConsultationsFinishedPaginatedListUseCase,
    private val consultationsFinishedByUserPreferences: ConsultationsByUserPreferencesUseCase,
    private val consultationPaginatedJsonMapper: ConsultationPaginatedJsonMapper,
) {

    @Operation(summary = "Get Consultations Terminées")
    @GetMapping("/consultations/finished/{pageNumber}")
    fun getConsultationFinishedList(
        @PathVariable pageNumber: Int,
        @RequestParam territory: String?,
    ): ResponseEntity<ConsultationPaginatedJson> {
        val consultations = if (territory == null) {
            consultationsFinishedByUserPreferences.execute(pageNumber)
        } else {
            consultationsFinishedByTerritoryUseCase
                .getConsultationFinishedPaginatedList(pageNumber, territory)
        }

        if (consultations == null) return ResponseEntity.notFound().build()

        return ResponseEntity.ok(consultationPaginatedJsonMapper.toJson(consultations))
    }
}
