package fr.social.gouv.agora.usecase.notification.repository

interface NotificationSendingRepository {
    fun sendNotificationMessage(request: NotificationRequest): NotificationResult
    fun sendQagDetailsNotification(request: NotificationRequest, qagId: String): NotificationResult
    fun sendNewConsultationNotification(request: ConsultationNotificationRequest)
    fun sendConsultationUpdateNotification(request: ConsultationNotificationRequest)
}

data class NotificationRequest(
    val fcmToken: String,
    val title: String,
    val description: String,
)

data class ConsultationNotificationRequest(
    val title: String,
    val description: String,
    val fcmTokenList: List<String>,
    val consultationId: String,
)

enum class NotificationResult {
    SUCCESS, FAILURE
}