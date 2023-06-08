package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.*
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import fr.social.gouv.agora.usecase.qag.QagResult

@RestController
@Suppress("unused")
class QagController(
    private val getQagUseCase: GetQagUseCase,
    private val insertQagUseCase: InsertQagUseCase,
    private val getAskQagStatusUseCase: GetAskQagStatusUseCase,
    private val mapper: QagJsonMapper,
) {

    @GetMapping("/qags/{qagId}")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable qagId: String,
    ): HttpEntity<*> {
        val qagResult = getQagUseCase.getQag(
            qagId = qagId,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return when (qagResult) {
            is QagResult.QagWithOpenOrAcceptedStatus -> ResponseEntity.ok(mapper.toJson(qagResult.qag))
            else -> ResponseEntity.EMPTY
        }
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