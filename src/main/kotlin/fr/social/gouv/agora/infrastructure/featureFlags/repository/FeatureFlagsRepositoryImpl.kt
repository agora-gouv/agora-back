package fr.social.gouv.agora.infrastructure.featureFlags.repository

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeatureFlagsRepositoryImpl(private val cacheManager: CacheManager) : FeatureFlagsRepository {

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
        AgoraFeature.AskQuestion -> "IS_ASK_QUESTION_ENABLED"
        AgoraFeature.QagSelect -> "IS_QAG_SELECT_ENABLED"
        AgoraFeature.QagArchive -> "IS_QAG_ARCHIVE_ENABLED"
        AgoraFeature.SimilarQag -> "IS_SIMILAR_QAG_ENABLED"
    }

    private fun getCache() = cacheManager.getCache(FEATURE_FLAGS_CACHE_NAME)

}