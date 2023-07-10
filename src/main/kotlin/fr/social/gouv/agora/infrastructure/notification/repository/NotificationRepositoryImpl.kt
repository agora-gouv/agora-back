package fr.social.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.*
import fr.social.gouv.agora.usecase.notification.repository.*
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class NotificationRepositoryImpl : NotificationRepository {

    companion object {
        private const val NOTIFICATION_TYPE_KEY = "type"

        private const val QAG_DETAILS_ID_KEY = "qagId"
        private const val CONSULTATION_DETAILS_ID_KEY = "consultationId"

        private const val QAG_DETAILS_NOTIFICATION_TYPE = "qagDetails"
        private const val CONSULTATION_DETAILS_NOTIFICATION_TYPE = "consultationDetails"
        private const val CONSULTATION_RESULTS_NOTIFICATION_TYPE = "consultationResults"
    }

    override fun sendNotificationMessage(request: NotificationRequest): NotificationResult {
        return try {
            sendNotification(notificationMessage = createBaseMessage(request).build())
        } catch (e: IllegalArgumentException) {
            NotificationResult.FAILURE
        }
    }

    override fun sendQagDetailsNotification(request: NotificationRequest, qagId: String): NotificationResult {
        return try {
            sendNotification(
                notificationMessage = createBaseMessage(request)
                    .putData(NOTIFICATION_TYPE_KEY, QAG_DETAILS_NOTIFICATION_TYPE)
                    .putData(QAG_DETAILS_ID_KEY, qagId)
                    .build()
            )
        } catch (e: IllegalArgumentException) {
            NotificationResult.FAILURE
        }
    }

    override fun sendNewConsultationNotification(request: ConsultationNotificationRequest): Pair<Int, Int>? {
        return try {
            val message = createMultiMessage(request = request, type = CONSULTATION_DETAILS_NOTIFICATION_TYPE)
            val response = FirebaseMessaging.getInstance().sendMulticast(message)
            Pair(response.successCount, request.fcmTokenList.size)
        } catch (e: Exception) {
            null
        }
    }

    override fun sendConsultationUpdateNotification(request: ConsultationNotificationRequest): Pair<Int, Int>? {
        return try {
            val message = createMultiMessage(request = request, type = CONSULTATION_RESULTS_NOTIFICATION_TYPE)
            val response = FirebaseMessaging.getInstance().sendMulticast(message)
            Pair(response.successCount, request.fcmTokenList.size)
        } catch (e: Exception) {
            null
        }
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

    private fun createMultiMessage(request: ConsultationNotificationRequest, type: String): MulticastMessage {
        return MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(request.title)
                    .setBody(request.description)
                    .build()
            )
            .putData(NOTIFICATION_TYPE_KEY, type)
            .putData(CONSULTATION_DETAILS_ID_KEY, request.consultationId)
            .addAllTokens(request.fcmTokenList)
            .build()
    }

    private fun sendNotification(notificationMessage: Message): NotificationResult {
        return try {
            val response = FirebaseMessaging.getInstance().send(notificationMessage)
            if (response.isNullOrEmpty())
                NotificationResult.FAILURE
            else NotificationResult.SUCCESS
        } catch (e: Exception) {
            NotificationResult.FAILURE
        }
    }

}
