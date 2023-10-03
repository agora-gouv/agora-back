package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.domain.NotificationInserting
import fr.social.gouv.agora.domain.NotificationType
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.ConsultationNotificationRequest
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
        for (userInfo in userList)
            notificationRepository.insertNotification(
                notification = NotificationInserting(
                    title = title,
                    description = description,
                    type = NotificationType.CONSULTATION,
                    userId = userInfo.userId
                )
            )
        notificationSendingRepository.sendNewConsultationNotification(
            request = ConsultationNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
                consultationId = consultationId,
            )
        )
    }

    fun sendConsultationUpdateNotification(
        title: String,
        description: String,
        consultationId: String,
    ) {
        val userList = userRepository.getAllUsers()
            .filter { userInfo ->
                getConsultationResponseRepository.hasAnsweredConsultation(
                    consultationId = consultationId,
                    userId = userInfo.userId,
                )
            }
        for (userInfo in userList)
            notificationRepository.insertNotification(
                notification = NotificationInserting(
                    title = title,
                    description = description,
                    type = NotificationType.CONSULTATION,
                    userId = userInfo.userId
                )
            )
        notificationSendingRepository.sendConsultationUpdateNotification(
            request = ConsultationNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
                consultationId = consultationId,
            )
        )
    }
}




