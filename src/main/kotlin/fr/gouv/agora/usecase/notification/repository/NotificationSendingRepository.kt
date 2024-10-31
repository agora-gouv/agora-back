package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.infrastructure.notification.TypeNotification
import fr.gouv.agora.usecase.notification.repository.MultiNotificationRequest.*

interface NotificationSendingRepository {
    fun sendGenericMultiNotification(request: GenericMultiNotificationRequest): NotificationResult
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

    // TODO SUPPR ?
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
        val type: TypeNotification,
        val pageArgument: String?,
    ) : MultiNotificationRequest()
}

enum class NotificationResult {
    SUCCESS, FAILURE
}
