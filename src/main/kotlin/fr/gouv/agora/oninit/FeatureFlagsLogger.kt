package fr.gouv.agora.oninit

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeatureFlagsLogger(private val featureFlagsRepository: FeatureFlagsRepository) : InitializingBean {

    override fun afterPropertiesSet() {
        println("Agora FeatureFlags:")
        println(
            AgoraFeature
                .values()
                .map { feature -> feature to featureFlagsRepository.isFeatureEnabled(feature) }
                .joinToString(separator = "\n") { (feature, isEnabled) -> "[$feature] $isEnabled" }
        )
    }

}