package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.ConsultationNotificationRequest
import fr.social.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class SendConsultationNotificationUseCase(
    private val userRepository: UserRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
) {
    fun sendNewConsultationNotification(
        title: String,
        description: String,
        consultationId: String,
    ): Pair<Int, Int>? {
        return notificationSendingRepository.sendNewConsultationNotification(
            request = ConsultationNotificationRequest(
                title = title,
                description = description,
                fcmTokenList = userRepository.getAllUsers().map { userInfo -> userInfo.fcmToken },
                consultationId = consultationId,
            )
        )
    }

    fun sendConsultationUpdateNotification(
        title: String,
        description: String,
        consultationId: String,
    ): Pair<Int, Int>? {
        return notificationSendingRepository.sendConsultationUpdateNotification(
            request = ConsultationNotificationRequest(
                title = title,
                description = description,
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
    }
}




