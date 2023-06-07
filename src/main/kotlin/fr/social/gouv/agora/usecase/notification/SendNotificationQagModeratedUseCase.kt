package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class SendNotificationQagModeratedUseCase(
    private val userRepository: UserRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationMessageRepository: NotificationMessageRepository,
) {

    fun sendNotificationQagModeratedMessage(qagId: String): NotificationResult {
        val userId = qagInfoRepository.getQagInfo(qagId = qagId)?.userId
        return if (userId != null)
            userRepository.getUserById(userId = userId)?.fcmToken?.let { fcmToken ->
                notificationRepository.sendNotificationMessage(
                    fcmToken = fcmToken,
                    title = notificationMessageRepository.getQagModeratedNotificationTitle(),
                    messageToSend = notificationMessageRepository.getQagModeratedNotificationMessage(),
                )
            } ?: NotificationResult.FAILURE
        else NotificationResult.FAILURE
    }
}

