package fr.social.gouv.agora.infrastructure.qagSimilar

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.infrastructure.qagModerating.QagModeratingJsonMapper
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.qagSimilar.FindSimilarQagUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class QagSimilarController(
    private val featureFlagsUseCase: FeatureFlagsUseCase,
    private val findSimilarQagUseCase: FindSimilarQagUseCase,
    private val mapper: QagModeratingJsonMapper,
) {

    @GetMapping("/qags/has_similar")
    fun hasSimilarQag(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("title") title: String,
    ): ResponseEntity<*> {
        if (!featureFlagsUseCase.isFeatureEnabled(AgoraFeature.SimilarQag)) {
            return ResponseEntity.badRequest().body(Unit)
        }

        val qagResult = findSimilarQagUseCase.findSimilarQags(
            writingQag = title,
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
        @RequestParam("title")
        title: String,
    ): ResponseEntity<*> {
        if (!featureFlagsUseCase.isFeatureEnabled(AgoraFeature.SimilarQag)) {
            return ResponseEntity.badRequest().body(Unit)
        }

        val qagResult = findSimilarQagUseCase.findSimilarQags(
            writingQag = title,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return if (qagResult.isEmpty())
            ResponseEntity.ok().body(Unit)
        else
            ResponseEntity.ok().body(mapper.toJson(qagResult))
    }

}