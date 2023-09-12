package fr.social.gouv.agora.usecase.featureFlags

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Service

@Service
class FeatureFlagsUseCase(private val featureFlagsRepository: FeatureFlagsRepository) {

    fun isFeatureEnabled(feature: AgoraFeature): Boolean {
        return featureFlagsRepository.isFeatureEnabled(feature)
    }

}