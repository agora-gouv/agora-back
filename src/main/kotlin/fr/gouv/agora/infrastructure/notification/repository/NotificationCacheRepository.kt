package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.infrastructure.notification.dto.NotificationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class NotificationCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val NOTIFICATION_CACHE_NAME = "notificationCache"
        private const val ALL_NOTIFICATION_CACHE_KEY = "notificationCacheList"
    }

    fun getCacheForUser(userId: String): List<NotificationDTO>? {
        return try {
            getCache()?.get("$ALL_NOTIFICATION_CACHE_KEY.$userId", List::class.java) as? List<NotificationDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun insertCacheForUser(userNotifications: List<NotificationDTO>, userId: String) {
        getCache()?.put("$ALL_NOTIFICATION_CACHE_KEY.$userId", userNotifications)
    }

    private fun getCache() = cacheManager.getCache(NOTIFICATION_CACHE_NAME)
}
