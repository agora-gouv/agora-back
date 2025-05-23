package fr.gouv.agora.infrastructure.featureFlags.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class FeatureFlagsRepositoryImpl(
    @Qualifier("eternalCacheManager") private val cacheManager: CacheManager,
) : FeatureFlagsRepository {

    companion object {
        private const val FEATURE_FLAGS_CACHE_NAME = "featureFlags"
    }

    override fun isFeatureEnabled(feature: AgoraFeature): Boolean {
        val featureKey = toKey(feature)
        val cachedValue = getCache()?.get(featureKey)?.get()?.let { it == true }

        return cachedValue ?: System.getenv(featureKey).toBoolean()
            .also { featureValue -> getCache()?.put(featureKey, featureValue) }
    }

    private fun toKey(feature: AgoraFeature) = when (feature) {
        AgoraFeature.SignUp -> "IS_SIGNUP_ENABLED"
        AgoraFeature.Login -> "IS_LOGIN_ENABLED"
        AgoraFeature.QagSelect -> "IS_QAG_SELECT_ENABLED"
        AgoraFeature.FeedbackResponseQag -> "IS_FEEDBACK_ON_RESPONSE_QAG_ENABLED"
        AgoraFeature.FeedbackConsultationUpdate -> "IS_FEEDBACK_ON_CONSULTATION_UPDATE_ENABLED"
        AgoraFeature.SuspiciousUserDetection -> "IS_SUSPICIOUS_USER_DETECTION_ENABLED"
        AgoraFeature.DeleteBannedUsersSupports -> "IS_DELETE_BANNED_USERS_SUPPORTS_ENABLED"
    }

    private fun getCache() = cacheManager.getCache(FEATURE_FLAGS_CACHE_NAME)

}
