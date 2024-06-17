package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class NotificationRepositoryImpl(
    private val databaseRepository: NotificationDatabaseRepository,
    private val cacheRepository: NotificationCacheRepository,
    private val mapper: NotificationMapper,
) : NotificationRepository {
    private val logger = LoggerFactory.getLogger(NotificationRepositoryImpl::class.java)

    override fun insertNotifications(notificationsToInsert: NotificationInserting) {
        val notifications = notificationsToInsert.let(mapper::toDto)

        if (notifications.isEmpty()) {
            logger.warn("insertNotifications - pas de notifications à insérer")
            return
        }

        databaseRepository.saveAll(notifications)
        // todo : est-ce que l'on ne récupérerait pas toutes les notifs de l'user pour remettre le cache à jour ?
    }

    override fun getUserNotificationList(userId: String): List<Notification> {
        val userUUID = userId.toUuidOrNull()
        if (userUUID == null) {
            logger.error("getUserNotificationList - impossible de convertir l'id '$userId' en UUID")
            return emptyList()
        }

        val cachedNotifications = cacheRepository.getCachedNotificationsForUser(userId)

        if (cachedNotifications != null) return cachedNotifications.map(mapper::toDomain)

        val databaseNotifications = databaseRepository.findAllByUserId(userUUID)

        cacheRepository.insertNotificationsToCacheForUser(databaseNotifications, userId)

        return databaseNotifications.map(mapper::toDomain)
    }

    override fun deleteUsersNotifications(userIDs: List<String>) {
        databaseRepository.deleteUsersNotifications(userIDs.mapNotNull { it.toUuidOrNull() })
    }
}
