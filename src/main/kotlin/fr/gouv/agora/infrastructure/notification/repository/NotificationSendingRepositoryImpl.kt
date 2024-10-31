package fr.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import fr.gouv.agora.usecase.notification.repository.GenericMultiNotificationRequest
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import fr.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.RejectedExecutionException

@Component
class NotificationSendingRepositoryImpl(
    private val taskScheduler: ThreadPoolTaskScheduler,
    private val clock: Clock,
) : NotificationSendingRepository {
    private val logger: Logger = LoggerFactory.getLogger(NotificationSendingRepositoryImpl::class.java)

    companion object {
        private const val NOTIFICATION_TYPE_KEY = "type"

        private const val MAX_SIMULTANEOUS_NOTIFICATIONS = 300
    }

    override fun sendGenericMultiNotification(request: GenericMultiNotificationRequest): NotificationResult {
        logger.info("📩 Sending multi-notification: ${request.title}")
        return try {
            sendMultiNotifications(createMultiMessage(request = request))
            NotificationResult.SUCCESS
        } catch (e: Exception) {
            logger.error("⚠️ Send réponse support notification error: ${e.message}")
            NotificationResult.FAILURE
        }
    }

    private fun createMultiMessage(request: GenericMultiNotificationRequest): List<MulticastMessage> {
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
                    .putData(NOTIFICATION_TYPE_KEY, request.type.name)
                    .addAllTokens(fcmTokenSubList)

                if (request.pageArgument != null) {
                    messageBuilder.putData("pageArgument", request.pageArgument)
                }

                messageBuilder.build()
            } catch (e: IllegalArgumentException) {
                logger.error("⚠️ Build multi-notification error on batch n°${index + 1}/${chunkedFcmTokenList.size}: ${e.message}")
                null
            }
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
            val response = FirebaseMessaging.getInstance().sendEachForMulticast(notificationMessage)
            logger.info("📩 Send multi-notification n°${batchIndex + 1}/$batchCount results:\n- ${response.responses.size} total responses\n- ${response.successCount} successes\n- ${response.failureCount} failures")
        } catch (e: FirebaseMessagingException) {
            logger.error("⚠️ Send multi-notification error on batch n°${batchIndex + 1}/$batchCount ${e.message}")
        }
    }
}
