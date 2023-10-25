package fr.social.gouv.agora.usecase.notification.repository

import fr.social.gouv.agora.usecase.notification.repository.MultiNotificationRequest.ConsultationMultiNotificationRequest
import fr.social.gouv.agora.usecase.notification.repository.MultiNotificationRequest.QagMultiNotificationRequest

interface NotificationSendingRepository {
    fun sendQagDetailsNotification(request: QagNotificationRequest): NotificationResult
    fun sendQagDetailsMultiNotification(request: QagMultiNotificationRequest)
    fun sendNewConsultationMultiNotification(request: ConsultationMultiNotificationRequest)
    fun sendConsultationUpdateMultiNotification(request: ConsultationMultiNotificationRequest)
    fun sendConsultationDeadlineMultiNotification(request: ConsultationMultiNotificationRequest)
}

data class QagNotificationRequest(
    val title: String,
    val description: String,
    val fcmToken: String,
    val qagId: String,
)

sealed class MultiNotificationRequest {
    abstract val title: String
    abstract val description: String
    abstract val fcmTokenList: List<String>

    data class QagMultiNotificationRequest(
        override val title: String,
        override val description: String,
        override val fcmTokenList: List<String>,
        val qagId: String,
    ) : MultiNotificationRequest()

    data class ConsultationMultiNotificationRequest(
        override val title: String,
        override val description: String,
        override val fcmTokenList: List<String>,
        val consultationId: String,
    ) : MultiNotificationRequest()
}

enum class NotificationResult {
    SUCCESS, FAILURE
}