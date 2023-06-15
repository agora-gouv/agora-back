package fr.social.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationRequest
import fr.social.gouv.agora.usecase.notification.repository.NotificationResult
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class NotificationRepositoryImpl : NotificationRepository {

    companion object {
        private const val NOTIFICATION_TYPE_KEY = "type"

        private const val QAG_DETAILS_NOTIFICATION_TYPE = "qagDetails"
        private const val QAG_DETAILS_ID_KEY = "qagId"
    }

    override fun sendNotificationMessage(request: NotificationRequest): NotificationResult {
        return sendNotification(
            notificationMessage = createBaseMessage(request).build()
        )
    }

    override fun sendQagDetailsNotification(request: NotificationRequest, qagId: String): NotificationResult {
        return sendNotification(
            notificationMessage = createBaseMessage(request)
                .putData(NOTIFICATION_TYPE_KEY, QAG_DETAILS_NOTIFICATION_TYPE)
                .putData(QAG_DETAILS_ID_KEY, qagId)
                .build()
        )
    }

    private fun createBaseMessage(request: NotificationRequest): Message.Builder {
        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(request.title)
                    .setBody(request.description)
                    .build()
            )
            .setToken(request.fcmToken)
    }

    private fun sendNotification(notificationMessage: Message): NotificationResult {
        return try {
            val response = FirebaseMessaging.getInstance().send(notificationMessage)
            if (response.isNullOrEmpty())
                NotificationResult.FAILURE
            else NotificationResult.SUCCESS
        } catch (e: FirebaseMessagingException) {
            NotificationResult.FAILURE
        }
    }

}
