package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.qag.GetQagModeratingListUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class QagModeratingController(
    private val getQagModeratingListUseCase: GetQagModeratingListUseCase,
    private val mapper: QagModeratingJsonMapper,
) {

    @GetMapping("/moderate/qags")
    fun getQagDetails(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): HttpEntity<*> {
        val qagModeratingList = getQagModeratingListUseCase.getQagModeratingList(
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return qagModeratingList.let { qagModerating ->
            ResponseEntity.ok(mapper.toJson(qagModerating, qagModeratingList.size))
        }
    }
}