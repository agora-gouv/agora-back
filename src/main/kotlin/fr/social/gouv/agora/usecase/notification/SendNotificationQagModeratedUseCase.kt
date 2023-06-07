package fr.social.gouv.agora.usecase.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.usecase.errorMessages.repository.ErrorMessagesRepository
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class SendNotificationQagModeratedUseCase(
    private val userRepository: UserRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val errorMessagesRepository: ErrorMessagesRepository,
) {

    fun sendNotificationQagModeratedMessage(qagId: String): NotificationResult {
        val userId = qagInfoRepository.getQagInfo(qagId = qagId)?.userId
        return if (userId != null) {
            userRepository.getUserById(userId = userId)?.fcmToken?.let { fcmToken ->
                val message = Message.builder()
                    .setNotification(
                        Notification.builder()
                            .setBody(errorMessagesRepository.getQagModeratedNotificationMessage())
                            .build())
                    .setToken(fcmToken)
                    .build()
                FirebaseMessaging.getInstance().send(message)
                NotificationResult.SUCCESS
            } ?: NotificationResult.FAILURE
        } else NotificationResult.FAILURE
    }
}

enum class NotificationResult {
    SUCCESS, FAILURE
}