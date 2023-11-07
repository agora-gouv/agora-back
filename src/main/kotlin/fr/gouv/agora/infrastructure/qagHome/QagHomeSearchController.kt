package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.infrastructure.utils.StringUtils.replaceDiacritics
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.qag.GetQagByKeywordsUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagHomeSearchController(
    private val getQagByKeywordsUseCase: GetQagByKeywordsUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
) {
    companion object {
        private const val MAX_CHARACTER_SIZE = 75
    }

    @GetMapping("/qags/search")
    fun getQagSearchPreviews(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("keywords") keywords: String?,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val filteredKeywords =
            keywords.takeUnless { it.isNullOrBlank() }?.take(MAX_CHARACTER_SIZE)?.let { replaceDiacritics(it) }
                ?.replace(Regex("[^A-Za-z0-9 ]"), "")
        return if (filteredKeywords.isNullOrBlank() || filteredKeywords.length < 3)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(Unit)
        else {
            ResponseEntity.ok().body(
                qagHomeJsonMapper.toJson(
                    getQagByKeywordsUseCase.getQagByKeywordsUseCase(
                        userId = userId,
                        keywords = filteredKeywords.split(" ").filterNot{ it.isBlank() },
                    )
                )
            )
        }
    }
}