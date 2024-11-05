package fr.gouv.agora.usecase.notification

import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.infrastructure.notification.TypeNotification
import fr.gouv.agora.usecase.consultation.exception.ConsultationNotFoundException
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.gouv.agora.usecase.notification.repository.NotificationResult
import fr.gouv.agora.usecase.notification.repository.NotificationSendingRepository
import org.springframework.stereotype.Service

@Service
class SendConsultationNotificationUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val userRepository: UserRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val notificationSendingRepository: NotificationSendingRepository,
    private val notificationRepository: NotificationRepository,
) {
    fun sendConsultationUpdateNotification(
        title: String,
        description: String,
        consultationId: String,
    ): NotificationResult {
        if (!consultationInfoRepository.isConsultationExists(consultationId))
            throw ConsultationNotFoundException(consultationId)

        val userAnsweredConsultationIds =
            userAnsweredConsultationRepository.getUsersAnsweredConsultation(consultationId = consultationId)
        val userList = userRepository.getAllUsers()
            .filter { userInfo -> userAnsweredConsultationIds.contains(userInfo.userId) }

        notificationSendingRepository.sendGenericMultiNotification(
            title = title,
            description = description,
            fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
            type = TypeNotification.DETAILS_CONSULTATION,
            pageArgument = consultationId
        )

        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = TypeNotification.DETAILS_CONSULTATION,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )

        return NotificationResult.SUCCESS
    }

    fun sendConsultationDeadlineNotification(
        title: String,
        description: String,
        consultationId: String,
    ): NotificationResult {
        if (!consultationInfoRepository.isConsultationExists(consultationId))
            throw ConsultationNotFoundException(consultationId)

        val userList = userRepository.getUsersNotAnsweredConsultation(consultationId = consultationId)

        notificationSendingRepository.sendGenericMultiNotification(
            title = title,
            description = description,
            fcmTokenList = userList.map { userInfo -> userInfo.fcmToken },
            type = TypeNotification.DETAILS_CONSULTATION,
            pageArgument = consultationId,
        )
        notificationRepository.insertNotifications(
            NotificationInserting(
                title = title,
                description = description,
                type = TypeNotification.DETAILS_CONSULTATION,
                userIds = userList.map { userInfo -> userInfo.userId },
            )
        )

        return NotificationResult.SUCCESS
    }
}
