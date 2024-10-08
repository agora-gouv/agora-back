package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.usecase.notification.repository.MultiNotificationRequest.*

interface NotificationSendingRepository {
    fun sendQagDetailsNotification(request: QagNotificationRequest): NotificationResult
    fun sendQagDetailsMultiNotification(request: QagMultiNotificationRequest)
    fun sendConsultationDetailsMultiNotification(request: ConsultationMultiNotificationRequest)
    fun sendConsultationUpdateMultiNotification(request: ConsultationMultiNotificationRequest)
    fun sendUserNotification(request: NotificationRequest): NotificationResult
    fun sendGenericMultiNotification(request: GenericMultiNotificationRequest)
}

data class NotificationRequest(
    val title: String,
    val description: String,
    val fcmToken: String,
)

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

    data class GenericMultiNotificationRequest(
        override val title: String,
        override val description: String,
        override val fcmTokenList: List<String>,
        val page: String,
        val pageArgument: String?,
    ) : MultiNotificationRequest()
}

enum class NotificationResult {
    SUCCESS, FAILURE
}
