package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.infrastructure.qagPaginated.QagPaginatedJsonMapper
import fr.gouv.agora.usecase.qag.GetQagCountUseCase
import fr.gouv.agora.usecase.qag.GetQagErrorTextUseCase
import fr.gouv.agora.usecase.qagPaginated.QagPaginatedV2UseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@Tag(name = "QaG")
class QagHomeV2Controller(
    private val qagPaginatedV2UseCase: QagPaginatedV2UseCase,
    private val getQagErrorTextUseCase: GetQagErrorTextUseCase,
    private val getQagCountUseCase: GetQagCountUseCase,
    private val qagPaginatedJsonMapper: QagPaginatedJsonMapper,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {
    @Operation(summary = "Get QaG Détails")
    @GetMapping("/v2/qags")
    fun getQagDetails(
        @RequestParam("pageNumber") pageNumber: String,
        @RequestParam("thematiqueId") thematiqueId: String?,
        @RequestParam("filterType") filterType: String,
    ): ResponseEntity<*> {
        val userId = authentificationHelper.getUserId()!!
        val usedThematiqueId = thematiqueId.takeUnless { it.isNullOrBlank() }
        val usedFilterType = filterType.takeUnless { it.isBlank() }

        val pageNumberInt = pageNumber.toIntOrNull() ?: return ResponseEntity.badRequest().body(Unit)

        val qagsAndMaxPageCount = when (usedFilterType) {
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
        }

        if (qagsAndMaxPageCount == null) {
            return ResponseEntity.badRequest().body(Unit)
        }

        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePrivate())
            .body(qagPaginatedJsonMapper.toJson(qagsAndMaxPageCount))
    }

    @Operation(summary = "Get QaG Status")
    @GetMapping("/qags/ask_status")
    fun askStatus(): ResponseEntity<*> {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePrivate())
            .body(
            qagHomeJsonMapper.toQagAskStatusJson(
                getQagErrorTextUseCase.getGetQagErrorText(
                    userId = authentificationHelper.getUserId()!!
                )
            )
        )
    }

    @Operation(summary = "Récupérer le nombre de QaGs sur la période en cours (non archivées et non sélectionnées")
    @GetMapping("/qags/count")
    fun getQagCount(): ResponseEntity<Int> {
        return ResponseEntity.ok().body(getQagCountUseCase.execute())
    }
}
