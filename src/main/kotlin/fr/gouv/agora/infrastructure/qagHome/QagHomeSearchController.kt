package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.infrastructure.utils.StringUtils.replaceDiacritics
import fr.gouv.agora.usecase.qag.GetQagByKeywordsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "QaG")
class QagHomeSearchController(
    private val getQagByKeywordsUseCase: GetQagByKeywordsUseCase,
    private val qagHomeJsonMapper: QagHomeJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {
    companion object {
        private const val MAX_CHARACTER_SIZE = 75
    }

    @Operation(summary = "Get QaG Recherche")
    @GetMapping("/qags/search")
    fun getQagSearchPreviews(
        @RequestParam("keywords") keywords: String?,
    ): ResponseEntity<*> {
        val userId = authentificationHelper.getUserId()!!
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
                        keywords = filteredKeywords.split(" ").filterNot { it.isBlank() },
                    )
                )
            )
        }
    }
}
