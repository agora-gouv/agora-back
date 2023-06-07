package fr.social.gouv.agora.usecase.notification.repository

interface NotificationRepository {
    fun sendNotificationMessage(fcmToken: String, title: String, messageToSend: String): NotificationResult
}

enum class NotificationResult {
    SUCCESS, FAILURE
}