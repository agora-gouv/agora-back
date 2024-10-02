package fr.gouv.agora.usecase.notification

import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.domain.NotificationType
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.notification.repository.MultiNotificationRequest
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import fr.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import org.springframework.stereotype.Service

@Service
class SendUsersNotificationUseCase(
    private val userRepository: UserRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationRepository: NotificationRepository,
) {
    fun execute(title: String, description: String, page: String, pageArgument: String?): NotificationResult {
        val userList = userRepository.getAllUsers().take(50) // TODO à supprimer

        notificationSendingRepository.sendGenericMultiNotification(
            MultiNotificationRequest.GenericMultiNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
                page = page,
                pageArgument = pageArgument,
            )
        )

        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = NotificationType.GENERIC,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )

        return NotificationResult.SUCCESS
    }
}
