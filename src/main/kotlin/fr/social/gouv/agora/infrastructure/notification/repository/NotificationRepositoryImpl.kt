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

    override fun getAllNotification(): List<Notification> {
        return getAllNotificationDTO().map(mapper::toDomain)
    }

    override fun insertNotification(notification: NotificationInserting): NotificationInsertionResult {
        return mapper.toDto(notification)?.let { notificationDTO ->
            val savedNotificationDTO = databaseRepository.save(notificationDTO)
            try {
                cacheRepository.insertNotification(notificationDTO = savedNotificationDTO)
            } catch (e: IllegalStateException) {
                val allNotificationDTO = databaseRepository.findAll()
                cacheRepository.initializeCache(allNotificationDTO + savedNotificationDTO)
            }
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

    @Suppress("UNCHECKED_CAST")
    private fun getAllNotificationDTO() = when (val cacheResult = cacheRepository.getAllNotificationList()) {
        is CacheResult.CachedNotificationList -> cacheResult.allNotificationDTO
        CacheResult.CacheNotInitialized -> databaseRepository.findAll().also { allNotificationDTO ->
            cacheRepository.initializeCache(allNotificationDTO as List<NotificationDTO>)
        }
    }
}



