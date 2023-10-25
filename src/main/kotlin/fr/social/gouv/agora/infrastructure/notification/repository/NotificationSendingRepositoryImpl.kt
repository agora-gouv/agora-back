package fr.social.gouv.agora.infrastructure.notification.repository

import com.google.firebase.messaging.*
import fr.social.gouv.agora.usecase.notification.repository.*
import fr.social.gouv.agora.usecase.notification.repository.MultiNotificationRequest.ConsultationMultiNotificationRequest
import fr.social.gouv.agora.usecase.notification.repository.MultiNotificationRequest.QagMultiNotificationRequest
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.time.*
import java.util.concurrent.RejectedExecutionException

@Component
@Suppress("unused")
class NotificationSendingRepositoryImpl(
    private val taskScheduler: ThreadPoolTaskScheduler,
    private val clock: Clock,
) : NotificationSendingRepository {

    companion object {
        private const val NOTIFICATION_TYPE_KEY = "type"

        private const val QAG_DETAILS_ID_KEY = "qagId"
        private const val CONSULTATION_DETAILS_ID_KEY = "consultationId"

        private const val QAG_DETAILS_NOTIFICATION_TYPE = "qagDetails"
        private const val CONSULTATION_DETAILS_NOTIFICATION_TYPE = "consultationDetails"
        private const val CONSULTATION_RESULTS_NOTIFICATION_TYPE = "consultationResults"
        private const val CONSULTATION_DEADLINE_NOTIFICATION_TYPE = "consultationDeadline"

        private const val MAX_SIMULTANEOUS_NOTIFICATIONS = 500
    }

    override fun sendQagDetailsNotification(request: QagNotificationRequest): NotificationResult {
        return try {
            sendNotification(
                notificationMessage = createBaseMessage(request)
                    .putData(NOTIFICATION_TYPE_KEY, QAG_DETAILS_NOTIFICATION_TYPE)
                    .putData(QAG_DETAILS_ID_KEY, request.qagId)
                    .build()
            )
        } catch (e: IllegalArgumentException) {
            println("⚠️ Send notification error: ${e.message}")
            NotificationResult.FAILURE
        }
    }

    override fun sendQagDetailsMultiNotification(request: QagMultiNotificationRequest) {
        println("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = QAG_DETAILS_NOTIFICATION_TYPE)
        )
    }

    override fun sendNewConsultationMultiNotification(request: ConsultationMultiNotificationRequest) {
        println("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = CONSULTATION_DETAILS_NOTIFICATION_TYPE)
        )
    }

    override fun sendConsultationUpdateMultiNotification(request: ConsultationMultiNotificationRequest) {
        println("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = CONSULTATION_RESULTS_NOTIFICATION_TYPE)
        )
    }

    override fun sendConsultationDeadlineMultiNotification(request: ConsultationMultiNotificationRequest) {
        println("📩 Sending multi-notification: ${request.title}")
        sendMultiNotifications(
            createMultiMessage(request = request, type = CONSULTATION_DEADLINE_NOTIFICATION_TYPE)
        )
    }

    private fun createBaseMessage(request: QagNotificationRequest): Message.Builder {
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
        return chunkedFcmTokenList.mapIndexedNotNull() { index, fcmTokenSubList ->
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
                println("⚠️ Build multi-notification error on batch n°${index + 1}/${chunkedFcmTokenList.size}: ${e.message}")
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
            println("⚠️ Send notification error: ${e.message}")
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
                println("⚠️ Send notification error when sending batch n°${index + 1}/${notificationMessages.size}: ${e.message}")
            }
        }
    }

}

class SendNotificationTask(
    private val notificationMessage: MulticastMessage,
    private val batchIndex: Int,
    private val batchCount: Int,
) : Runnable {

    override fun run() {
        try {
            val response = FirebaseMessaging.getInstance().sendMulticast(notificationMessage)
            println("📩 Send multi-notification n°${batchIndex + 1}/$batchCount results:\n- ${response.responses.size} total responses\n- ${response.successCount} successes\n- ${response.failureCount} failures")
        } catch (e: FirebaseMessagingException) {
            println("⚠️ Send multi-notification error on batch n°${batchIndex + 1}/$batchCount ${e.message}")
        }
    }

}