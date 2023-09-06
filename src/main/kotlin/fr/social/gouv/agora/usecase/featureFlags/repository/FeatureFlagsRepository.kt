package fr.social.gouv.agora.usecase.featureFlags.repository

import fr.social.gouv.agora.domain.AgoraFeature

interface FeatureFlagsRepository {
    fun isFeatureEnabled(feature: AgoraFeature): Boolean
}