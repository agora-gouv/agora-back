package fr.social.gouv.agora.usecase.notification.repository

interface NotificationRepository {
    fun sendNotificationMessage(request: NotificationRequest): NotificationResult
    fun sendQagDetailsNotification(request: NotificationRequest, qagId: String): NotificationResult
    fun sendNewConsultationNotification(request: NewConsultationNotificationRequest): Pair<Int, Int>?
    fun sendConsultationUpdateNotification(request: NewConsultationNotificationRequest): Pair<Int, Int>?
}

data class NotificationRequest(
    val fcmToken: String,
    val title: String,
    val description: String,
)

data class NewConsultationNotificationRequest(
    val title: String,
    val description: String,
    val fcmTokenList: List<String>,
    val consultationId: String,
)

enum class NotificationResult {
    SUCCESS, FAILURE
}