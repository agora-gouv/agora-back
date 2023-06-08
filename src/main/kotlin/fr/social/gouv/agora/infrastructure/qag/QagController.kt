package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.*
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagController(
    private val getQagUseCase: GetQagUseCase,
    private val insertQagUseCase: InsertQagUseCase,
    private val getAskQagStatusUseCase: GetAskQagStatusUseCase,
    private val getQagInfoUseCase: GetQagInfoUseCase,
    private val mapper: QagJsonMapper,
) {

    @GetMapping("/qags/{qagId}")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
    ): HttpEntity<*> {
        val qagStatus = getQagInfoUseCase.getQagStatus(qagId)
        return if (qagStatus == QagStatus.OPEN || qagStatus == QagStatus.MODERATED_ACCEPTED)
            getQagUseCase.getQag(
                qagId = qagId,
                userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
            )?.let { qag ->
                ResponseEntity.ok(mapper.toJson(qag))
            } ?: ResponseEntity.EMPTY
        else
            ResponseEntity.EMPTY
    }

    @PostMapping("/qags")
    fun insertQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody qagInsertingJson: QagInsertingJson,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return if (getAskQagStatusUseCase.getAskQagStatus(userId = userId) == AskQagStatus.ENABLED) {
            when (val result = insertQagUseCase.insertQag(mapper.toDomain(json = qagInsertingJson, userId = userId))) {
                QagInsertionResult.Failure -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
                is QagInsertionResult.Success -> ResponseEntity.ok().body(mapper.toJson(result))
            }
        } else
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("")
    }
}