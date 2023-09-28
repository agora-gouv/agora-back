package fr.social.gouv.agora.infrastructure.moderatus.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ModeratusQagLockCacheRepository(
    @Qualifier("eternalCacheManager") private val cacheManager: CacheManager,
) {

    companion object {
        private const val MODERATUS_QAG_LOCK_CACHE_NAME = "ModeratusQagLockCache"
        private const val LOCKED_QAG_IDS_KEY = "lockedQagIds"
    }

    @Suppress("UNCHECKED_CAST")
    fun getLockedQagIds(): List<String> {
        return try {
            getCache()?.get(LOCKED_QAG_IDS_KEY, List::class.java) as? List<String>
        } catch (e: IllegalStateException) {
            null
        } ?: emptyList()
    }

    fun addLockedIds(lockedQagIds: List<String>) {
        getCache()?.put(LOCKED_QAG_IDS_KEY, (getLockedQagIds() + lockedQagIds).distinct())
    }

    fun removeLockedQagId(qagId: String) {
        getCache()?.put(LOCKED_QAG_IDS_KEY, getLockedQagIds().filter { it != qagId })
    }

    private fun getCache() = cacheManager.getCache(MODERATUS_QAG_LOCK_CACHE_NAME)

}