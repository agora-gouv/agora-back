package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.infrastructure.notification.dto.NotificationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NotificationCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val NOTIFICATION_CACHE_NAME = "notificationCache"
        private const val ALL_NOTIFICATION_CACHE_KEY = "notificationCacheList"
    }

    sealed class CacheResult {
        data class CachedNotificationList(val allNotificationDTO: List<NotificationDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun initializeCache(allNotificationDTO: List<NotificationDTO>) {
        getCache()?.put(ALL_NOTIFICATION_CACHE_KEY, allNotificationDTO)
    }

    fun getAllNotificationList(): CacheResult {
        return when (val allNotificationDTO = getAllNotificationDTOFromCache()) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedNotificationList(allNotificationDTO)
        }
    }

    fun insertNotification(notificationDTOList: List<NotificationDTO>) {
        getAllNotificationDTOFromCache()?.let { allNotificationDTO ->
            initializeCache(allNotificationDTO + notificationDTOList)
        }
    }

    private fun getCache() = cacheManager.getCache(NOTIFICATION_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getAllNotificationDTOFromCache(): List<NotificationDTO>? {
        return try {
            getCache()?.get(ALL_NOTIFICATION_CACHE_KEY, List::class.java) as? List<NotificationDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }
}