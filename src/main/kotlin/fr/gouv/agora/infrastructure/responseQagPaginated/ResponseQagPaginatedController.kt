package fr.gouv.agora.infrastructure.responseQagPaginated

import fr.gouv.agora.usecase.responseQag.GetResponseQagPreviewPaginatedListUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ResponseQagPaginatedController(
    private val getResponseQagPreviewPaginatedListUseCase: GetResponseQagPreviewPaginatedListUseCase,
    private val responseQagPaginatedJsonMapper: ResponseQagPaginatedJsonMapper,
) {

    @GetMapping("/qags/responses/{pageNumber}")
    fun getQagResponses(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            getResponseQagPaginatedJson(pageNumberInt)?.let { ResponseEntity.ok(it) }
        } ?: ResponseEntity.badRequest().body(Unit)
    }

    @Deprecated("Should use GET /qags/responses/{pageNumber} instead")
    @GetMapping("/qags/responses/page/{pageNumber}")
    fun getQagResponsesPaginated(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            getResponseQagPaginatedJson(pageNumberInt)?.let { ResponseEntity.ok(it) }
        } ?: ResponseEntity.badRequest().body(Unit)
    }

    private fun getResponseQagPaginatedJson(pageNumber: Int): ResponseQagPaginatedJson? {
        return getResponseQagPreviewPaginatedListUseCase.getResponseQagPreviewPaginatedList(pageNumber = pageNumber)
            ?.let(responseQagPaginatedJsonMapper::toJson)
    }
}