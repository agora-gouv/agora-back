package fr.social.gouv.agora.infrastructure.qagSimilar

import fr.social.gouv.agora.infrastructure.qag.QagModeratingJsonMapper
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.FindSimilarQagUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@Suppress("unused")
class QagSimilarController(
    private val findSimilarQagUseCase: FindSimilarQagUseCase,
    private val mapper: QagModeratingJsonMapper,
) {

    @GetMapping("/qags/has_similar")
    fun hasSimilarQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("title") title: String,
    ): ResponseEntity<*> {
        val qagResult = findSimilarQagUseCase.findSimilarQag(
            question = title,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return if (qagResult.isEmpty())
            ResponseEntity.ok().body(QagHasSimilarJson(hasSimilar = false))
        else
            ResponseEntity.ok().body(QagHasSimilarJson(hasSimilar = true))
    }

    @GetMapping("/qags/similar")
    fun getSimilarQagList(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("title") title: String,
    ): ResponseEntity<*> {
        val qagResult = findSimilarQagUseCase.findSimilarQag(
            question = title,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return if (qagResult.isEmpty())
            ResponseEntity.ok().body("")
        else
            ResponseEntity.ok().body(mapper.toJson(qagResult))
    }
}