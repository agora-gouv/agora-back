package fr.social.gouv.agora.infrastructure.qagPaginated

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qagPaginated.QagPaginatedUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagPaginatedController(
    private val qagPaginatedUseCase: QagPaginatedUseCase,
    private val qagPaginatedJsonMapper: QagPaginatedJsonMapper,
) {
    companion object {
        private const val MAX_CHARACTER_SIZE = 75
    }

    @GetMapping("/qags/page/{pageNumber}")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable pageNumber: String,
        @RequestParam("filterType") filterType: String?,
        @RequestParam("thematiqueId") thematiqueId: String?,
        @RequestParam("keywords") keywords: String?,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val usedFilterType = filterType.takeUnless { it.isNullOrBlank() }
        val usedThematiqueId = thematiqueId.takeUnless { it.isNullOrBlank() }
        val usedPageNumber = pageNumber.toIntOrNull()
        val filtredKeywords = keywords.takeUnless { it.isNullOrBlank() }?.take(MAX_CHARACTER_SIZE)

        return usedPageNumber?.let { pageNumberInt ->
            when (usedFilterType) {
                "popular" -> qagPaginatedUseCase.getPopularQagPaginated(
                    userId = userId,
                    pageNumber = pageNumberInt,
                    thematiqueId = usedThematiqueId,
                    keywords = filtredKeywords,
                )

                "latest" -> qagPaginatedUseCase.getLatestQagPaginated(
                    userId = userId,
                    pageNumber = pageNumberInt,
                    thematiqueId = usedThematiqueId,
                    keywords = filtredKeywords,
                )

                "supporting" -> qagPaginatedUseCase.getSupportedQagPaginated(
                    userId = userId,
                    pageNumber = pageNumberInt,
                    thematiqueId = usedThematiqueId,
                    keywords = filtredKeywords,
                )

                else -> null
            }?.let { qagsAndMaxPageCount ->
                ResponseEntity.ok(qagPaginatedJsonMapper.toJson(qagsAndMaxPageCount))
            } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
        } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)

    }

}