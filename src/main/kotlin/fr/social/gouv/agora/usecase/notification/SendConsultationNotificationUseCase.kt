package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.usecase.login.repository.UserRepository
import fr.social.gouv.agora.usecase.notification.repository.*
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class SendConsultationNotificationUseCase(
    private val userRepository: UserRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
    private val notificationRepository: NotificationRepository,
) {
    fun sendNewConsultationNotification(
        title: String,
        description: String,
        consultationId: String,
    ): Pair<Int, Int>? {
        return notificationRepository.sendNewConsultationNotification(
            request = NewConsultationNotificationRequest(
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
        return notificationRepository.sendNewConsultationNotification(
            request = NewConsultationNotificationRequest(
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




