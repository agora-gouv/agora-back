package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationType
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.usecase.notification.NotificationListAndHasMoreNotificationsFlag
import org.springframework.stereotype.Component

@Component
class NotificationPaginatedJsonMapper(private val dateMapper: DateMapper) {

    companion object {
        private const val QAG_NOTIFICATION_TYPE = "Questions citoyennes"
        private const val CONSULTATION_NOTIFICATION_TYPE = "Consultations"
    }

    fun toJson(notificationListAndHasMoreNotificationsFlag: NotificationListAndHasMoreNotificationsFlag): NotificationPaginatedJson {
        return NotificationPaginatedJson(
            hasMoreNotifications = notificationListAndHasMoreNotificationsFlag.hasMoreNotifications,
            notifications = notificationListAndHasMoreNotificationsFlag.notifications.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: Notification): NotificationJson {
        return NotificationJson(
            title = "${domain.title} ${domain.description}",
            type = when (domain.type) {
                NotificationType.CONSULTATION -> CONSULTATION_NOTIFICATION_TYPE
                NotificationType.QAG -> QAG_NOTIFICATION_TYPE
            },
            date = dateMapper.toFormattedDate(domain.date),
        )
    }

}