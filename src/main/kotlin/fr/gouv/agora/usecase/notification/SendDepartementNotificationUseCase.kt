package fr.gouv.agora.usecase.notification

import fr.gouv.agora.domain.Department
import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.infrastructure.notification.TypeNotification
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import fr.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import org.springframework.stereotype.Service

@Service
class SendDepartementNotificationUseCase(
    private val userRepository: UserRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationRepository: NotificationRepository,
) {
    fun execute(
        departement: Department,
        title: String,
        description: String,
        pageArgument: String?,
        typeNotification: TypeNotification
    ): NotificationResult {
        val userList =
            userRepository.getUsersLivingInDepartement(departement) +
                    userRepository.getUsersInterestedInDepartement(departement)

        notificationSendingRepository.sendGenericMultiNotification(
            title = title,
            description = description,
            fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
            pageArgument = pageArgument,
            type = typeNotification
        )

        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = typeNotification,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )

        return NotificationResult.SUCCESS
    }
}
