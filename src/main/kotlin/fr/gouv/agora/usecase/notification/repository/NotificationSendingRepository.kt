package fr.gouv.agora.usecase.notification.repository

import fr.gouv.agora.infrastructure.notification.TypeNotification

interface NotificationSendingRepository {
    fun sendGenericMultiNotification(
        title: String,
        description: String,
        fcmTokenList: List<String>,
        type: TypeNotification,
        pageArgument: String?
    ): NotificationResult
}

enum class NotificationResult {
    SUCCESS, FAILURE
}
