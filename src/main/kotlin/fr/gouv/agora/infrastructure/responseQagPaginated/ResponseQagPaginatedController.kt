package fr.gouv.agora.infrastructure.responseQagPaginated

import fr.gouv.agora.usecase.responseQag.GetResponseQagPreviewPaginatedListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class ResponseQagPaginatedController(
    private val getResponseQagPreviewPaginatedListUseCase: GetResponseQagPreviewPaginatedListUseCase,
    private val responseQagPaginatedJsonMapper: ResponseQagPaginatedJsonMapper,
) {

    @GetMapping("/qags/responses/page/{pageNumber}")
    fun getQagDetails(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        val usedPageNumber = pageNumber.toIntOrNull()

        return usedPageNumber?.let { pageNumberInt ->
            getResponseQagPreviewPaginatedListUseCase.getResponseQagPreviewPaginatedList(pageNumber = pageNumberInt)
        }?.let { responseQagPaginatedList ->
            ResponseEntity.ok(responseQagPaginatedJsonMapper.toJson(responseQagPaginatedList))
        } ?: ResponseEntity.badRequest().body(Unit)
    }
}