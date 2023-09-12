package fr.social.gouv.agora.infrastructure.notification.repository

import fr.social.gouv.agora.domain.Notification
import fr.social.gouv.agora.domain.NotificationInserting
import fr.social.gouv.agora.domain.NotificationType
import fr.social.gouv.agora.infrastructure.notification.dto.NotificationDTO
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class NotificationMapper {

    companion object {
        private const val QAG_NOTIFICATION_TYPE = "Questions citoyennes"
        private const val CONSULTATION_NOTIFICATION_TYPE = "Consultations"
    }

    fun toDomain(dto: NotificationDTO): Notification {
        return Notification(
            title = dto.title,
            type = when (dto.type) {
                QAG_NOTIFICATION_TYPE -> NotificationType.QAG
                CONSULTATION_NOTIFICATION_TYPE -> NotificationType.CONSULTATION
                else -> throw IllegalArgumentException("Invalid Notification type : ${dto.type}")
            },
            date = dto.date,
            userId = dto.userId.toString(),
        )
    }

    fun toDto(domain: NotificationInserting): NotificationDTO? {
        return try {
            NotificationDTO(
                id = UuidUtils.NOT_FOUND_UUID,
                title = domain.title,
                type = when (domain.type) {
                    NotificationType.QAG -> QAG_NOTIFICATION_TYPE
                    NotificationType.CONSULTATION -> CONSULTATION_NOTIFICATION_TYPE
                },
                date = LocalDate.now().toDate(),
                userId = UUID.fromString(domain.userId),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}