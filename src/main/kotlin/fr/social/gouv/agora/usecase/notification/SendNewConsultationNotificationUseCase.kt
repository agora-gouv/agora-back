package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.*
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class SendNewConsultationNotificationUseCase(
    private val userRepository: UserRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
    private val notificationRepository: NotificationRepository,
) {
    companion object {
        private const val CONSULTATION_DETAILS_NOTIFICATION_TYPE = "consultationDetails"
        private const val CONSULTATION_RESULTS_NOTIFICATION_TYPE = "consultationResults"
    }

    fun sendNewConsultationNotification(
        title: String,
        description: String,
        type: String,
        consultationId: String,
    ): Int? {
        return when (type) {
            CONSULTATION_DETAILS_NOTIFICATION_TYPE -> notificationRepository.sendNewConsultationNotification(
                request = NewConsultationNotificationRequest(
                    title = title,
                    description = description,
                    type = type,
                    fcmTokenList = userRepository.getAllUsers().map { userInfo -> userInfo.fcmToken },
                    consultationId = consultationId,
                )
            )

            CONSULTATION_RESULTS_NOTIFICATION_TYPE -> notificationRepository.sendNewConsultationNotification(
                request = NewConsultationNotificationRequest(
                    title = title,
                    description = description,
                    type = type,
                    fcmTokenList = userRepository.getAllUsers()
                        .filter { userInfo ->
                            getConsultationResponseRepository.hasAnsweredConsultation(
                                consultationId = consultationId,
                                userId = userInfo.userId,
                            )
                        }.map { userInfo -> userInfo.fcmToken },
                    consultationId = consultationId,
                )
            )

            else -> null
        }
    }
}




