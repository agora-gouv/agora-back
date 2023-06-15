package fr.social.gouv.agora.usecase.notification.repository

interface NotificationRepository {
    fun sendNotificationMessage(request: NotificationRequest): NotificationResult
    fun sendQagDetailsNotification(request: NotificationRequest, qagId: String): NotificationResult
}

data class NotificationRequest(
    val fcmToken: String,
    val title: String,
    val description: String,
)

enum class NotificationResult {
    SUCCESS, FAILURE
}