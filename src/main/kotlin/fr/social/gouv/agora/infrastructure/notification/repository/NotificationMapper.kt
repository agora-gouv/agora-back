package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.domain.Notification
import fr.social.gouv.agora.domain.NotificationType
import fr.social.gouv.agora.infrastructure.notification.dto.NotificationDTO
import org.springframework.stereotype.Component

@Component
class NotificationMapper {

    companion object {
        private const val QAG_DETAILS_NOTIFICATION_TYPE = "qagDetails"
        private const val CONSULTATION_DETAILS_NOTIFICATION_TYPE = "consultationDetails"
        private const val CONSULTATION_RESULTS_NOTIFICATION_TYPE = "consultationResults"
    }

    fun toDomain(dto: NotificationDTO): Notification {
        return Notification(
            id = dto.id.toString(),
            title = dto.title,
            type = when (dto.type) {
                QAG_DETAILS_NOTIFICATION_TYPE -> NotificationType.QAG
                CONSULTATION_DETAILS_NOTIFICATION_TYPE -> NotificationType.CONSULTATION
                CONSULTATION_RESULTS_NOTIFICATION_TYPE -> NotificationType.CONSULTATION
                else -> throw IllegalArgumentException("Invalid Notification type : ${dto.type}")
            },
            date = dto.date,
            userId = dto.userId.toString(),
        )
    }
}