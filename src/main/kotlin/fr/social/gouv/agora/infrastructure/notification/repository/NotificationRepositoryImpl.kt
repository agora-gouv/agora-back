package fr.social.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationResult
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class NotificationRepositoryImpl : NotificationRepository {

    override fun sendNotificationMessage(fcmToken: String, title: String, messageToSend: String): NotificationResult {
        val message = Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(messageToSend)
                    .build()
            )
            .setToken(fcmToken).build()

        return try {
            val response = FirebaseMessaging.getInstance().send(message)
            if (response.isNullOrEmpty())
                NotificationResult.FAILURE
            else NotificationResult.SUCCESS
        } catch (e: FirebaseMessagingException) {
            NotificationResult.FAILURE
        }
    }

}
