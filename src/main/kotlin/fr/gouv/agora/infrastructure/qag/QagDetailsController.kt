package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qag.GetPublicQagDetailsUseCase
import fr.gouv.agora.usecase.qag.GetQagDetailsUseCase
import fr.gouv.agora.usecase.qag.QagResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagDetailsController(
    private val getQagDetailsUseCase: GetQagDetailsUseCase,
    private val mapper: QagJsonMapper,
    private val getPublicQagDetailsUseCase: GetPublicQagDetailsUseCase,
    private val publicQagJsonMapper: PublicQagJsonMapper,
) {

    @GetMapping("/qags/{qagId}")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        val qagResult = getQagDetailsUseCase.getQagDetails(
            qagId = qagId,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return when (qagResult) {
            is QagResult.Success -> ResponseEntity.ok(mapper.toJson(qagResult.qag))
            QagResult.QagRejectedStatus -> ResponseEntity.status(HttpStatus.LOCKED).body(Unit)
            QagResult.QagNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Unit)
        }
    }

    @GetMapping("/public/qags/{qagId}")
    fun getPublicQagDetails(
        @PathVariable qagId: String,
    ): ResponseEntity<*> {
        return getPublicQagDetailsUseCase.getQagDetails(qagId = qagId)?.let { qag ->
            ResponseEntity.ok(publicQagJsonMapper.toJson(qag))
        } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(Unit)
    }

}