package fr.social.gouv.agora.usecase.notification.repository

import fr.social.gouv.agora.domain.Notification

interface NotificationRepository {
    fun getAllNotification(): List<Notification>
    fun insertNotification(notification: Notification): NotificationInsertionResult
    fun getUserNotificationList(userId: String): List<Notification>
}

enum class NotificationInsertionResult {
    SUCCESS, FAILURE
}