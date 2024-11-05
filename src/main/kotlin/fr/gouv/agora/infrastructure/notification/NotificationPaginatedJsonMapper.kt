package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.usecase.notification.NotificationListAndHasMoreNotificationsFlag
import org.springframework.stereotype.Component

@Component
class NotificationPaginatedJsonMapper(private val dateMapper: DateMapper) {

    companion object {
        private const val QAG_NOTIFICATION_TYPE = "Questions citoyennes"
        private const val CONSULTATION_NOTIFICATION_TYPE = "Consultations"
        private const val REPONSE_SUPPORT_NOTIFICATION_TYPE = "Réponse du support"
    }

    fun toJson(notificationListAndHasMoreNotificationsFlag: NotificationListAndHasMoreNotificationsFlag): NotificationPaginatedJson {
        return NotificationPaginatedJson(
            hasMoreNotifications = notificationListAndHasMoreNotificationsFlag.hasMoreNotifications,
            notifications = notificationListAndHasMoreNotificationsFlag.notifications.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: Notification): NotificationJson {
        return NotificationJson(
            title = domain.title,
            description = domain.description,
            type = when (domain.type) {
                TypeNotification.ALL_REPONSES_QAGS -> QAG_NOTIFICATION_TYPE
                TypeNotification.HOME_QAGS -> QAG_NOTIFICATION_TYPE
                TypeNotification.DETAILS_QAG -> QAG_NOTIFICATION_TYPE
                TypeNotification.HOME_CONSULTATIONS -> CONSULTATION_NOTIFICATION_TYPE
                TypeNotification.DETAILS_CONSULTATION -> CONSULTATION_NOTIFICATION_TYPE
                TypeNotification.REPONSE_SUPPORT -> REPONSE_SUPPORT_NOTIFICATION_TYPE
            },
            date = dateMapper.toFormattedDate(domain.date),
        )
    }
}
