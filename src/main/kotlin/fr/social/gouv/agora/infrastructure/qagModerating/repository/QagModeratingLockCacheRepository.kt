package fr.social.gouv.agora.infrastructure.qagModerating.repository

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

    fun getUserIdForQagLocked(qagUUID: UUID): String? {
        return try {
            getCache()?.get(qagUUID.toString(), String::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun setQagLocked(qagUUID: UUID, userUUID: UUID) {
        getCache()?.put(qagUUID.toString(), userUUID.toString())
    }

    private fun getCache() = cacheManager.getCache(QAG_MODERATING_LOCK_CACHE_NAME)
}

