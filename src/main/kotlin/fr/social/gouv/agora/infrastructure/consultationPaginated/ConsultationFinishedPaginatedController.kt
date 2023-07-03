package fr.social.gouv.agora.infrastructure.consultationPaginated

import fr.social.gouv.agora.usecase.consultationPaginated.GetConsultationFinishedPaginatedListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class ConsultationFinishedPaginatedController(
    private val getConsultationFinishedPaginatedListUseCase: GetConsultationFinishedPaginatedListUseCase,
    private val consultationFinishedPaginatedJsonMapper: ConsultationFinishedPaginatedJsonMapper,
) {

    @GetMapping("/consultations/finished/{pageNumber}")
    fun getConsultationFinishedList(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        val usedPageNumber = pageNumber.toIntOrNull()

        return usedPageNumber?.let { pageNumberInt ->
            getConsultationFinishedPaginatedListUseCase.getConsultationFinishedPaginatedList(pageNumber = pageNumberInt)
        }?.let { consultationFinishedPaginatedList ->
            ResponseEntity.ok(consultationFinishedPaginatedJsonMapper.toJson(consultationFinishedPaginatedList))
        } ?: ResponseEntity.badRequest().body(Unit)
    }
}