package fr.gouv.agora.usecase.notification

import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.domain.NotificationType
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.gouv.agora.usecase.notification.repository.NotificationRequest
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import fr.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import org.springframework.stereotype.Service

@Service
class SendUserNotificationUseCase(
    private val userRepository: UserRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationRepository: NotificationRepository,
) {
    fun execute(title: String, description: String, userId: String): NotificationResult {
        val user = userRepository.getUserById(userId) ?: throw UserIdInconnuException(userId)
        if (user.fcmToken.isBlank()) throw FcmTokenVideException()

        notificationSendingRepository.sendUserNotification(
            request = NotificationRequest(
                title = title,
                description = description,
                fcmToken = user.fcmToken,
            )
        )
        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = NotificationType.REPONSE_SUPPORT,
                userIds = listOf(userId),
            )
        )
        return NotificationResult.SUCCESS
    }
}
