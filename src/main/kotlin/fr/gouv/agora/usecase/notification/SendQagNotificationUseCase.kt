package fr.gouv.agora.usecase.notification

import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.domain.NotificationType
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.notification.repository.MultiNotificationRequest
import fr.gouv.agora.usecase.notification.repository.NotificationMessageRepository
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import fr.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import fr.gouv.agora.usecase.notification.repository.QagNotificationRequest
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class SendQagNotificationUseCase(
    private val userRepository: UserRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationMessageRepository: NotificationMessageRepository,
    private val notificationRepository: NotificationRepository,
) {

    fun sendNotificationQagUpdate(title: String, description: String, qagId: String): NotificationResult {
        if (qagInfoRepository.getQagInfo(qagId) == null) return NotificationResult.FAILURE

        val userList = userRepository.getAllUsers()
        notificationSendingRepository.sendQagDetailsMultiNotification(
            request = MultiNotificationRequest.QagMultiNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
                qagId = qagId,
            )
        )
        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = NotificationType.QAG,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )

        return NotificationResult.SUCCESS
    }

    fun sendNotificationQagRejected(qagId: String): NotificationResult {
        val notificationMessage = notificationMessageRepository.getQagRejected()

        return sendNotification(
            qagId = qagId,
            title = notificationMessage.title,
            description = notificationMessage.message,
        )
    }

    fun sendNotificationQagAccepted(qagId: String): NotificationResult {
        val notificationMessage = notificationMessageRepository.getQagAccepted()

        return sendNotification(
            qagId = qagId,
            title = notificationMessage.title,
            description = notificationMessage.message,
        )
    }

    fun sendNotificationQagAcceptedAfterReject(qagId: String): NotificationResult {
        val notificationMessage = notificationMessageRepository.getQagAcceptedAfterReject()

        return sendNotification(
            qagId = qagId,
            title = notificationMessage.title,
            description = notificationMessage.message,
        )
    }


    private fun sendNotification(qagId: String, title: String, description: String): NotificationResult {
        if (qagInfoRepository.getQagInfo(qagId) == null) return NotificationResult.FAILURE

        val (userId, fcmToken) = getQagAuthorFcmToken(qagId = qagId)
        val sendingNotificationResult = fcmToken?.let {
            notificationSendingRepository.sendQagDetailsNotification(
                request = QagNotificationRequest(
                    title = title,
                    description = description,
                    fcmToken = fcmToken,
                    qagId = qagId,
                ),
            )
        } ?: NotificationResult.FAILURE
        userId?.let {
            notificationRepository.insertNotifications(
                NotificationInserting(
                    title = title,
                    description = description,
                    type = NotificationType.QAG,
                    userIds = listOf(userId),
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
