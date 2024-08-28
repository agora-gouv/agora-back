package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.usecase.qag.AskQagStatus
import fr.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class AskQagStatusCacheRepositoryImpl(
    private val cacheManager: CacheManager,
) : AskQagStatusCacheRepository {

    companion object {
        private const val ASK_QAG_STATUS_CACHE_NAME = "askQagStatus"
        private const val STATUS_FEATURE_DISABLED = "FEATURE_DISABLED"
        private const val STATUS_WEEKLY_LIMIT_REACHED = "WEEKLY_LIMIT_REACHED"
        private const val STATUS_ENABLED = "ENABLED"
    }

    override fun getAskQagStatus(userId: String): AskQagStatus? {
        return fromCachedValued(getCache()?.get(userId, String::class.java))
    }

    override fun initAskQagStatus(userId: String, status: AskQagStatus) {
        getCache()?.put(userId, toCachedValue(status))
    }

    override fun evictAskQagStatus(userId: String) {
        getCache()?.evict(userId)
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(ASK_QAG_STATUS_CACHE_NAME)

    private fun toCachedValue(status: AskQagStatus) = when (status) {
        AskQagStatus.FEATURE_DISABLED -> STATUS_FEATURE_DISABLED
        AskQagStatus.WEEKLY_LIMIT_REACHED -> STATUS_WEEKLY_LIMIT_REACHED
        AskQagStatus.ENABLED -> STATUS_ENABLED
    }

    private fun fromCachedValued(cachedValue: String?) = when (cachedValue) {
        STATUS_FEATURE_DISABLED -> AskQagStatus.FEATURE_DISABLED
        STATUS_WEEKLY_LIMIT_REACHED -> AskQagStatus.WEEKLY_LIMIT_REACHED
        STATUS_ENABLED -> AskQagStatus.ENABLED
        else -> null
    }
}
