package fr.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.*
import fr.gouv.agora.usecase.notification.repository.*
import fr.gouv.agora.usecase.notification.repository.MultiNotificationRequest.ConsultationMultiNotificationRequest
import fr.gouv.agora.usecase.notification.repository.MultiNotificationRequest.QagMultiNotificationRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.time.*
import java.util.concurrent.RejectedExecutionException

@Component
class NotificationSendingRepositoryImpl(
    private val taskScheduler: ThreadPoolTaskScheduler,
    private val clock: Clock,
) : NotificationSendingRepository {
    private val logger: Logger = LoggerFactory.getLogger(NotificationSendingRepositoryImpl::class.java)

    companion object {
        private const val NOTIFICATION_TYPE_KEY = "type"

        private const val QAG_DETAILS_ID_KEY = "qagId"
        private const val CONSULTATION_DETAILS_ID_KEY = "consultationId"

        private const val QAG_DETAILS_NOTIFICATION_TYPE = "qagDetails"
        private const val CONSULTATION_DETAILS_NOTIFICATION_TYPE = "consultationDetails"
        private const val CONSULTATION_RESULTS_NOTIFICATION_TYPE = "consultationResults"
        private const val REPONSE_SUPPORT_NOTIFICATION_TYPE = "reponseSupport"

        private const val MAX_SIMULTANEOUS_NOTIFICATIONS = 500
    }

    override fun sendQagDetailsNotification(request: QagNotificationRequest): NotificationResult {
        return try {
            sendNotification(
                notificationMessage = createQagNotificationBaseMessage(request)
                    .putData(NOTIFICATION_TYPE_KEY, QAG_DETAILS_NOTIFICATION_TYPE)
                    .putData(QAG_DETAILS_ID_KEY, request.qagId)
                    .build()
            )
        } catch (e: IllegalArgumentException) {
            logger.error("⚠️ Send notification error: ${e.message}")
            NotificationResult.FAILURE
        }
    }

    override fun sendQagDetailsMultiNotification(request: QagMultiNotificationRequest) {
        logger.info("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = QAG_DETAILS_NOTIFICATION_TYPE)
        )
    }

    override fun sendConsultationDetailsMultiNotification(request: ConsultationMultiNotificationRequest) {
        logger.info("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = CONSULTATION_DETAILS_NOTIFICATION_TYPE)
        )
    }

    override fun sendConsultationUpdateMultiNotification(request: ConsultationMultiNotificationRequest) {
        logger.info("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = CONSULTATION_RESULTS_NOTIFICATION_TYPE)
        )
    }

    override fun sendUserNotification(request: NotificationRequest): NotificationResult {
        return try {
            sendNotification(
                notificationMessage = createBaseMessage(request)
                    .putData(NOTIFICATION_TYPE_KEY, REPONSE_SUPPORT_NOTIFICATION_TYPE)
                    .build()
            )
        } catch (e: IllegalArgumentException) {
            logger.error("⚠️ Send réponse support notification error: ${e.message}")
            NotificationResult.FAILURE
        }
    }

    private fun createQagNotificationBaseMessage(request: QagNotificationRequest): Message.Builder {
        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(request.title)
                    .setBody(request.description)
                    .build()
            )
            .setToken(request.fcmToken)
    }

    private fun createBaseMessage(request: NotificationRequest): Message.Builder {
        return Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(request.title)
                    .setBody(request.description)
                    .build()
            )
            .setToken(request.fcmToken)
    }

    private fun createMultiMessage(request: MultiNotificationRequest, type: String): List<MulticastMessage> {
        val chunkedFcmTokenList = request
            .fcmTokenList
            .filter { it.isNotBlank() }
            .distinct()
            .chunked(MAX_SIMULTANEOUS_NOTIFICATIONS)
        return chunkedFcmTokenList.mapIndexedNotNull { index, fcmTokenSubList ->
            try {
                val messageBuilder = MulticastMessage.builder()
                    .setNotification(
                        Notification.builder()
                            .setTitle(request.title)
                            .setBody(request.description)
                            .build()
                    )
                    .putData(NOTIFICATION_TYPE_KEY, type)
                    .addAllTokens(fcmTokenSubList)

                when (request) {
                    is ConsultationMultiNotificationRequest -> messageBuilder.putData(
                        CONSULTATION_DETAILS_ID_KEY,
                        request.consultationId,
                    )
                    is QagMultiNotificationRequest -> messageBuilder.putData(
                        QAG_DETAILS_ID_KEY,
                        request.qagId,
                    )
                }
                messageBuilder.build()
            } catch (e: IllegalArgumentException) {
                logger.error("⚠️ Build multi-notification error on batch n°${index + 1}/${chunkedFcmTokenList.size}: ${e.message}")
                null
            }
        }
    }

    private fun sendNotification(notificationMessage: Message): NotificationResult {
        return try {
            val response = FirebaseMessaging.getInstance().send(notificationMessage)
            if (response.isNullOrEmpty())
                NotificationResult.FAILURE
            else NotificationResult.SUCCESS
        } catch (e: Exception) {
            logger.error("⚠️ Send notification error: ${e.message}")
            NotificationResult.FAILURE
        }
    }

    private fun sendMultiNotifications(notificationMessages: List<MulticastMessage>) {
        val dateNow = LocalDateTime.now(clock)
        notificationMessages.forEachIndexed { index, notificationMessage ->
            try {
                taskScheduler.schedule(
                    SendNotificationTask(
                        notificationMessage = notificationMessage,
                        batchIndex = index,
                        batchCount = notificationMessages.size,
                    ),
                    dateNow.plusMinutes(index.toLong()).atZone(ZoneId.systemDefault()).toInstant(),
                )
            } catch (e: RejectedExecutionException) {
                logger.error("⚠️ Send notification error when sending batch n°${index + 1}/${notificationMessages.size}: ${e.message}")
            }
        }
    }

}

class SendNotificationTask(
    private val notificationMessage: MulticastMessage,
    private val batchIndex: Int,
    private val batchCount: Int,
) : Runnable {
    private val logger: Logger = LoggerFactory.getLogger(SendNotificationTask::class.java)

    override fun run() {
        try {
            val response = FirebaseMessaging.getInstance().sendMulticast(notificationMessage)
            logger.info("📩 Send multi-notification n°${batchIndex + 1}/$batchCount results:\n- ${response.responses.size} total responses\n- ${response.successCount} successes\n- ${response.failureCount} failures")
        } catch (e: FirebaseMessagingException) {
            logger.error("⚠️ Send multi-notification error on batch n°${batchIndex + 1}/$batchCount ${e.message}")
        }
    }

}
