package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.infrastructure.qagPaginated.QagPaginatedJsonMapper
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.gouv.agora.usecase.qagPaginated.QagPaginatedV2UseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
@Tag(name = "QaG")
class QagHomeV2Controller(
    private val qagPaginatedV2UseCase: QagPaginatedV2UseCase,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val qagPaginatedJsonMapper: QagPaginatedJsonMapper,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {

    @Operation(summary = "Get QaG Détails")
    @GetMapping("/v2/qags")
    fun getQagDetails(
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
        @RequestParam("pageNumber") pageNumber: String,
        @RequestParam("thematiqueId") thematiqueId: String?,
        @RequestParam("filterType") filterType: String,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val usedThematiqueId = thematiqueId.takeUnless { it.isNullOrBlank() }
        val usedFilterType = filterType.takeUnless { it.isBlank() }

        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
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

                "trending" -> qagPaginatedV2UseCase.getTrendingQag(userId = userId)

                else -> null
            }?.let { qagsAndMaxPageCount ->
                ResponseEntity.ok().body(qagPaginatedJsonMapper.toJson(qagsAndMaxPageCount))
            } ?: ResponseEntity.badRequest().body(Unit)
        } ?: ResponseEntity.badRequest().body(Unit)
    }

    @Operation(summary = "Get QaG Status")
    @GetMapping("/qags/ask_status")
    fun askStatus(@RequestHeader("Authorization", required = false) authorizationHeader: String): ResponseEntity<*> {
        return ResponseEntity.ok().body(
            qagHomeJsonMapper.toQagAskStatusJson(
                getQagErrorTextUseCase.getGetQagErrorText(
                    userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
                )
            )
        )
    }

}
