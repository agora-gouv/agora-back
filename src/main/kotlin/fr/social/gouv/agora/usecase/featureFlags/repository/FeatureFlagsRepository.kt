package fr.social.gouv.agora.usecase.featureFlags.repository

import fr.social.gouv.agora.domain.FeatureFlags

interface FeatureFlagsRepository {
    fun getFeatureFlags(): FeatureFlags
}