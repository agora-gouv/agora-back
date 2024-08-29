package fr.gouv.agora.infrastructure.responseQagPaginated

import fr.gouv.agora.usecase.responseQag.GetResponseQagPreviewPaginatedListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "QaG")
class ResponseQagPaginatedController(
    private val getResponseQagPreviewPaginatedListUseCase: GetResponseQagPreviewPaginatedListUseCase,
    private val responseQagPaginatedJsonMapper: ResponseQagPaginatedJsonMapper,
) {

    @Operation(summary = "Get QaG Responses Paginés")
    @GetMapping("/qags/responses/{pageNumber}")
    fun getQagResponses(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            getResponseQagPaginatedJson(pageNumberInt)?.let { ResponseEntity.ok(it) }
        } ?: ResponseEntity.badRequest().body(Unit)
    }

    @Deprecated("Should use GET /qags/responses/{pageNumber} instead, can be deleted once required app version is <TBD> (development not started yet)")
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
