package fr.social.gouv.agora.infrastructure.qag.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class QagModeratingLockCacheRepository(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
) {
    companion object {
        private const val QAG_MODERATING_LOCK_CACHE_NAME = "QagModeratingLockCache"
    }

    fun isQagLocked(qagUUID: UUID): Boolean {
        return try {
            getCache()?.get(qagUUID.toString(), String::class.java)!=null
        } catch (e: IllegalStateException) {
            false
        }
    }

    fun setQagLocked(qagUUID: UUID) {
        getCache()?.put(qagUUID.toString(), qagUUID.toString())
    }

    private fun getCache() = cacheManager.getCache(QAG_MODERATING_LOCK_CACHE_NAME)
}

