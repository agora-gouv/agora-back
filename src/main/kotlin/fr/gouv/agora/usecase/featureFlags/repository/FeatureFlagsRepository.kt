package fr.gouv.agora.usecase.featureFlags.repository

import fr.gouv.agora.domain.AgoraFeature

interface FeatureFlagsRepository {
    fun isFeatureEnabled(feature: AgoraFeature): Boolean
}