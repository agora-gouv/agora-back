package fr.social.gouv.agora.usecase.notification.repository

interface NotificationRepository {
    fun sendNotificationMessage(request: NotificationRequest): NotificationResult
    fun sendQagDetailsNotification(request: NotificationRequest, qagId: String): NotificationResult
    fun sendNewConsultationNotification(request: NewConsultationNotificationRequest): Pair<Int, Int>?
}

data class NotificationRequest(
    val fcmToken: String,
    val title: String,
    val description: String,
)

data class NewConsultationNotificationRequest(
    val title: String,
    val description: String,
    val type: Type,
    val fcmTokenList: List<String>,
    val consultationId: String,
)

enum class Type {
    CONSULTATION_DETAILS_NOTIFICATION_TYPE, CONSULTATION_RESULTS_NOTIFICATION_TYPE
}

enum class NotificationResult {
    SUCCESS, FAILURE
}