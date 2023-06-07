package fr.social.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationResult
import org.springframework.stereotype.Component

@Component
class NotificationRepositoryImpl : NotificationRepository {

    override fun sendNotificationMessage(fcmToken: String, messageToSend: String): NotificationResult {
        val message = Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle("Question non conforme à la Charte")
                    .setBody(messageToSend)
                    .build())
            .setToken(fcmToken).build()
        val response = FirebaseMessaging.getInstance().send(message)
        return if (response.isNullOrEmpty())
            NotificationResult.FAILURE
        else NotificationResult.SUCCESS
    }
}
