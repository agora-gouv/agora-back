package fr.gouv.agora.oninit

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeatureFlagsLogger(private val featureFlagsRepository: FeatureFlagsRepository) : InitializingBean {
    private val logger: Logger = LoggerFactory.getLogger(FeatureFlagsLogger::class.java)

    override fun afterPropertiesSet() {
        logger.info("Agora FeatureFlags:")
        logger.info(
            AgoraFeature
                .values()
                .map { feature -> feature to featureFlagsRepository.isFeatureEnabled(feature) }
                .joinToString(separator = "\n") { (feature, isEnabled) -> "[$feature] $isEnabled" }
        )
    }

}
