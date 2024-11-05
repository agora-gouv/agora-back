package fr.gouv.agora.infrastructure.responseQagPaginated

import fr.gouv.agora.usecase.responseQag.GetResponseQagPreviewPaginatedListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

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
        val toIntOrNull = pageNumber.toIntOrNull() ?: return ResponseEntity.badRequest().body(Unit)

        return getResponseQagPaginatedJson(toIntOrNull).let {
            ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(it)
        }
    }

    private fun getResponseQagPaginatedJson(pageNumber: Int): ResponseQagPaginatedJson? {
        return getResponseQagPreviewPaginatedListUseCase.getResponseQagPreviewPaginatedList(pageNumber = pageNumber)
            ?.let(responseQagPaginatedJsonMapper::toJson)
    }
}
