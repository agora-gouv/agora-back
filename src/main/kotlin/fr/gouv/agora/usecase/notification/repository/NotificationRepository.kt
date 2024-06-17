package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationInserting

interface NotificationRepository {
    fun insertNotifications(notificationsToInsert: NotificationInserting)
    fun getUserNotificationList(userId: String): List<Notification>
    fun deleteUsersNotifications(userIDs: List<String>)
}

enum class NotificationInsertionResult {
    SUCCESS, FAILURE
}
