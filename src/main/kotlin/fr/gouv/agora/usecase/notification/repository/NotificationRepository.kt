package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationInserting

interface NotificationRepository {
    fun insertNotifications(notification: NotificationInserting): NotificationInsertionResult
    fun getUserNotificationList(userId: String): List<Notification>
}

enum class NotificationInsertionResult {
    SUCCESS, FAILURE
}