package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.qag.GetPublicQagDetailsUseCase
import fr.gouv.agora.usecase.qag.GetQagDetailsUseCase
import fr.gouv.agora.usecase.qag.QagResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "QaG")
class QagDetailsController(
    private val getQagDetailsUseCase: GetQagDetailsUseCase,
    private val mapper: QagJsonMapper,
    private val getPublicQagDetailsUseCase: GetPublicQagDetailsUseCase,
    private val publicQagJsonMapper: PublicQagJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {
    @Operation(summary = "Get QaG Détails")
    @GetMapping("/qags/{qagId}")
    fun getQagDetails(
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        val qagResult = getQagDetailsUseCase.getQagDetails(
            qagId = qagId,
            userId = authentificationHelper.getUserId()!!,
        )
        return when (qagResult) {
            is QagResult.Success -> ResponseEntity.ok(mapper.toJson(qagResult.qag))
            QagResult.QagRejectedStatus -> ResponseEntity.status(HttpStatus.LOCKED).body(Unit)
            QagResult.QagNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Unit)
        }
    }

    @Tag(name = "Public")
    @Operation(summary = "Get Public QaG")
    @GetMapping("/api/public/qags/{qagId}")
    fun getPublicQagDetails(
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        return getPublicQagDetailsUseCase.getQagDetails(qagId = qagId)?.let { qag ->
            ResponseEntity.ok(publicQagJsonMapper.toJson(qag))
        } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(Unit)
    }

}
