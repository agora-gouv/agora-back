package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.domain.NotificationInserting
import fr.social.gouv.agora.domain.NotificationType
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.*
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class SendNotificationQagModeratedUseCase(
    private val userRepository: UserRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationMessageRepository: NotificationMessageRepository,
    private val notificationRepository: NotificationRepository,
) {

    fun sendNotificationQagRejected(qagId: String): NotificationResult {
        return sendNotification(
            qagId = qagId,
            title = notificationMessageRepository.getQagRejectedTitle(),
            description = notificationMessageRepository.getQagRejectedMessage(),
        )
    }

    fun sendNotificationQagAccepted(qagId: String): NotificationResult {
        return sendNotification(
            qagId = qagId,
            title = notificationMessageRepository.getQagAcceptedTitle(),
            description = notificationMessageRepository.getQagAcceptedMessage(),
        )
    }

    fun sendNotificationQagAcceptedAfterReject(qagId: String): NotificationResult {
        return sendNotification(
            qagId = qagId,
            title = notificationMessageRepository.getQagAcceptedAfterRejectTitle(),
            description = notificationMessageRepository.getQagAcceptedAfterRejectTitle(),
        )
    }

    private fun sendNotification(qagId: String, title: String, description: String): NotificationResult {
        val (userId, fcmToken) = getQagAuthorFcmToken(qagId = qagId)
        val sendingNotificationResult = fcmToken?.let {
            notificationSendingRepository.sendQagDetailsNotification(
                request = NotificationRequest(
                    fcmToken = fcmToken,
                    title = title,
                    description = description,
                ),
                qagId = qagId,
            )
        } ?: NotificationResult.FAILURE
        userId?.let {
            notificationRepository.insertNotification(
                NotificationInserting(
                    title = title,
                    description = description,
                    type = NotificationType.QAG,
                    userId = userId
                )
            )
        }
        return sendingNotificationResult
    }

    private fun getQagAuthorFcmToken(qagId: String): Pair<String?, String?> {
        val userId = qagInfoRepository.getQagInfo(qagId = qagId)?.userId
        val fcmToken = userId?.let { userRepository.getUserById(userId = userId)?.fcmToken }
        return Pair(userId, fcmToken)
    }
}

