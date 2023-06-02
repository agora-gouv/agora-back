package fr.social.gouv.agora.usecase.featureFlags

import fr.social.gouv.agora.domain.FeatureFlags
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Service

@Service
class FeatureFlagsUseCase(private val featureFlagsRepository: FeatureFlagsRepository) {
    fun getFeatureFlags(): FeatureFlags {
        return featureFlagsRepository.getFeatureFlags()
    }
}