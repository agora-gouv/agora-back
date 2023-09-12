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

        val sendingNotificationResult = getQagAuthorFcmToken(qagId = qagId)?.let { fcmToken ->
            notificationSendingRepository.sendQagDetailsNotification(
                request = NotificationRequest(
                    fcmToken = fcmToken,
                    title = title,
                    description = description,
                ),
                qagId = qagId,
            )
        } ?: NotificationResult.FAILURE
        when (sendingNotificationResult) {
            NotificationResult.SUCCESS -> qagInfoRepository.getQagInfo(qagId = qagId)?.userId?.let {
                notificationRepository.insertNotification(
                    NotificationInserting(
                        title = title,
                        type = NotificationType.QAG,
                        userId = it
                    )
                )
            }
            else -> {} //do nothing
        }
        return sendingNotificationResult
    }

    private fun getQagAuthorFcmToken(qagId: String): String? {
        return qagInfoRepository.getQagInfo(qagId = qagId)?.userId?.let { userId ->
            userRepository.getUserById(userId = userId)?.fcmToken
        }
    }
}

