package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.infrastructure.notification.TypeNotification
import fr.gouv.agora.infrastructure.notification.dto.NotificationDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class NotificationMapper {

    fun toDomain(dto: NotificationDTO): Notification {
        return Notification(
            title = dto.title,
            description = dto.description,
            type = dto.type,
            date = dto.date,
            userId = dto.userId.toString(),
        )
    }

    fun toDto(domain: NotificationInserting): List<NotificationDTO> {
        val nowDate = Date()
        return domain.userIds.mapNotNull { userId ->
            userId.toUuidOrNull()?.let { userUUID ->
                NotificationDTO(
                    id = UuidUtils.NOT_FOUND_UUID,
                    title = domain.title,
                    description = domain.description,
                    type = domain.type,
                    date = nowDate,
                    userId = userUUID,
                )
            }
        }
    }
}
