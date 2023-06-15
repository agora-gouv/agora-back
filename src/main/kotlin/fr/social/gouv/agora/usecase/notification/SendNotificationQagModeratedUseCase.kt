package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.social.gouv.agora.usecase.notification.repository.NotificationRequest
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

    fun sendNotificationQagRejected(qagId: String): NotificationResult {
        return getQagAuthorFcmToken(qagId = qagId)?.let { fcmToken ->
            notificationRepository.sendNotificationMessage(
                request = NotificationRequest(
                    fcmToken = fcmToken,
                    title = notificationMessageRepository.getQagRejectedTitle(),
                    description = notificationMessageRepository.getQagRejectedMessage(),
                )
            )
        } ?: NotificationResult.FAILURE
    }

    fun sendNotificationQagAccepted(qagId: String): NotificationResult {
        return getQagAuthorFcmToken(qagId = qagId)?.let { fcmToken ->
            notificationRepository.sendQagDetailsNotification(
                request = NotificationRequest(
                    fcmToken = fcmToken,
                    title = notificationMessageRepository.getQagAcceptedTitle(),
                    description = notificationMessageRepository.getQagAcceptedMessage(),
                ),
                qagId = qagId,
            )
        } ?: NotificationResult.FAILURE
    }

    private fun getQagAuthorFcmToken(qagId: String): String? {
        return qagInfoRepository.getQagInfo(qagId = qagId)?.userId?.let { userId ->
            userRepository.getUserById(userId = userId)?.fcmToken
        }
    }
}

