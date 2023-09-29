package fr.social.gouv.agora.infrastructure.moderatus.repository

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class ModeratusQagLockCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val MODERATUS_QAG_LOCK_CACHE_NAME = "ModeratusQagLock"
        private const val LOCKED_QAG_IDS_KEY = "lockedQagIds"
    }

    sealed class CacheResult {
        data class CachedLockedQagIds(val lockedQagIds: List<String>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getLockedQagIds(): CacheResult {
        return when (val lockedQagIds = getLockedQagIdsFromCache()) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedLockedQagIds(lockedQagIds)
        }
    }

    fun addLockedQagIds(lockedQagIds: List<String>) {
        getLockedQagIdsFromCache()?.let { oldLockedQagIds ->
            getCache()?.put(LOCKED_QAG_IDS_KEY, (oldLockedQagIds + lockedQagIds).distinct())
        }
    }

    fun removeLockedQagId(qagId: String) {
        getLockedQagIdsFromCache()?.let { oldLockedQagIds ->
            getCache()?.put(LOCKED_QAG_IDS_KEY, oldLockedQagIds.filter { lockedQagId -> lockedQagId != qagId })
        }
    }

    private fun getCache() = cacheManager.getCache(MODERATUS_QAG_LOCK_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getLockedQagIdsFromCache(): List<String>? {
        return try {
            getCache()?.get(LOCKED_QAG_IDS_KEY, List::class.java) as? List<String>
        } catch (e: IllegalStateException) {
            null
        }
    }

}