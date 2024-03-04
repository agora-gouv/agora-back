package fr.gouv.agora.infrastructure.consultationPaginated

import fr.gouv.agora.usecase.consultationPaginated.ConsultationsFinishedPaginatedListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class ConsultationFinishedPaginatedController(
    private val consultationsFinishedPaginatedListUseCase: ConsultationsFinishedPaginatedListUseCase,
    private val consultationFinishedPaginatedJsonMapper: ConsultationFinishedPaginatedJsonMapper,
) {

    @GetMapping("/consultations/finished/{pageNumber}")
    fun getConsultationFinishedList(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        val usedPageNumber = pageNumber.toIntOrNull()

        return usedPageNumber?.let { pageNumberInt ->
            consultationsFinishedPaginatedListUseCase.getConsultationFinishedPaginatedList(pageNumber = pageNumberInt)
        }?.let { consultationFinishedPaginatedList ->
            ResponseEntity.ok(consultationFinishedPaginatedJsonMapper.toJson(consultationFinishedPaginatedList))
        } ?: ResponseEntity.badRequest().body(Unit)
    }
}