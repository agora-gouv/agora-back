package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.infrastructure.qagPaginated.QagPaginatedJsonMapper
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qagPaginated.QagPaginatedV2UseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagHomeV2Controller(
    private val qagPaginatedV2UseCase: QagPaginatedV2UseCase,
    private val qagPaginatedJsonMapper: QagPaginatedJsonMapper,
) {

    @GetMapping("/v2/qags")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("pageNumber") pageNumber: String,
        @RequestParam("thematiqueId") thematiqueId: String?,
        @RequestParam("filterType") filterType: String,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val usedPageNumber = pageNumber.toIntOrNull()
        val usedThematiqueId = thematiqueId.takeUnless { it.isNullOrBlank() }
        val usedFilterType = filterType.takeUnless { it.isBlank() }

        return usedPageNumber?.let { pageNumberInt ->
            when (usedFilterType) {
                "top" -> qagPaginatedV2UseCase.getPopularQagPaginated(
                    userId = userId,
                    pageNumber = pageNumberInt,
                    thematiqueId = usedThematiqueId,
                )

                "latest" -> qagPaginatedV2UseCase.getLatestQagPaginated(
                    userId = userId,
                    pageNumber = pageNumberInt,
                    thematiqueId = usedThematiqueId,
                )

                "supporting" -> qagPaginatedV2UseCase.getSupportedQagPaginated(
                    userId = userId,
                    pageNumber = pageNumberInt,
                    thematiqueId = usedThematiqueId,
                )

                else -> null
            }?.let { qagsAndMaxPageCount ->
                ResponseEntity.ok(qagPaginatedJsonMapper.toJson(qagsAndMaxPageCount))
            } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
        } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)

    }

}