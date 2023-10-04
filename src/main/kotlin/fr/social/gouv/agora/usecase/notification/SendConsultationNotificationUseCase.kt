package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.domain.NotificationInserting
import fr.social.gouv.agora.domain.NotificationType
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.MultiNotificationRequest.ConsultationMultiNotificationRequest
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class SendConsultationNotificationUseCase(
    private val userRepository: UserRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationRepository: NotificationRepository,
) {
    fun sendNewConsultationNotification(
        title: String,
        description: String,
        consultationId: String,
    ) {
        val userList = userRepository.getAllUsers()
        notificationSendingRepository.sendNewConsultationMultiNotification(
            request = ConsultationMultiNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
                consultationId = consultationId,
            )
        )
        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = NotificationType.CONSULTATION,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )
    }

    fun sendConsultationUpdateNotification(
        title: String,
        description: String,
        consultationId: String,
    ) {
        val userAnsweredConsultationIds =
            getConsultationResponseRepository.getUsersAnsweredConsultation(consultationId = consultationId)
        val userList = userRepository.getAllUsers()
            .filter { userInfo -> userAnsweredConsultationIds.contains(userInfo.userId) }

        notificationSendingRepository.sendConsultationUpdateMultiNotification(
            request = ConsultationMultiNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
                consultationId = consultationId,
            )
        )
        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = NotificationType.CONSULTATION,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )
    }
}




