package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.domain.Notification
import fr.social.gouv.agora.domain.NotificationInserting
import fr.social.gouv.agora.infrastructure.notification.dto.NotificationDTO
import fr.social.gouv.agora.infrastructure.notification.repository.NotificationCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.notification.repository.NotificationInsertionResult
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class NotificationRepositoryImpl(
    private val databaseRepository: NotificationDatabaseRepository,
    private val cacheRepository: NotificationCacheRepository,
    private val mapper: NotificationMapper,
) : NotificationRepository {

    override fun insertNotifications(notification: NotificationInserting): NotificationInsertionResult {
        return mapper.toDto(notification).takeUnless { it.isEmpty() }
            ?.let { notificationDTOList ->
                val savedNotificationDTOList = databaseRepository.saveAll(notificationDTOList)
                cacheRepository.insertNotification(savedNotificationDTOList.toList())
                NotificationInsertionResult.SUCCESS
            } ?: NotificationInsertionResult.FAILURE
    }

    override fun getUserNotificationList(userId: String): List<Notification> {
        return try {
            val userUUID = UUID.fromString(userId)
            getAllNotificationDTO()
                .filter { notificationDTO -> notificationDTO.userId == userUUID }
                .map(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getAllNotificationDTO() = when (val cacheResult = cacheRepository.getAllNotificationList()) {
        is CacheResult.CachedNotificationList -> cacheResult.allNotificationDTO
        CacheResult.CacheNotInitialized -> getAllNotifactionFromDatabaseAndInitializeCache()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getAllNotifactionFromDatabaseAndInitializeCache() =
        databaseRepository.findAll().also { allNotificationDTO ->
            cacheRepository.initializeCache(allNotificationDTO as List<NotificationDTO>)
        }
}



