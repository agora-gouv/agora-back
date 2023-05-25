package fr.social.gouv.agora.infrastructure.featureFlags

import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")

class FeatureFlagsController(
    private val featureFlagsUseCase: FeatureFlagsUseCase,
    private val jsonMapper: FeatureFlagsJsonMapper
) {
    @GetMapping("/featureFlags")
    fun getFeatureFlags(): ResponseEntity<FeatureFlagsJson> {
        return ResponseEntity.ok().body(jsonMapper.toJson((featureFlagsUseCase.getFeatureFlags())))
    }
}
