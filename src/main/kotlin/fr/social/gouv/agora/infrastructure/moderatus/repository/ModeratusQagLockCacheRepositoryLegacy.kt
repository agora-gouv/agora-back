package fr.social.gouv.agora.infrastructure.moderatus.repository

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ModeratusQagLockCacheRepositoryLegacy(private val cacheManager: CacheManager) {

    companion object {
        private const val MODERATUS_QAG_LOCK_CACHE_NAME = "ModeratusQagLockCache"
        private const val LOCKED_QAG_IDS_KEY = "lockedQagIds"
    }

    @Suppress("UNCHECKED_CAST")
    @Deprecated("Should use getLockedQagIds from ModeratusQagLockCacheRepository instead")
    fun getLockedQagIds(): List<String> {
        return try {
            getCache()?.get(LOCKED_QAG_IDS_KEY, List::class.java) as? List<String>
        } catch (e: IllegalStateException) {
            null
        } ?: emptyList()
    }

    fun clear() {
        getCache()?.evict(LOCKED_QAG_IDS_KEY)
    }

    private fun getCache() = cacheManager.getCache(MODERATUS_QAG_LOCK_CACHE_NAME)

}