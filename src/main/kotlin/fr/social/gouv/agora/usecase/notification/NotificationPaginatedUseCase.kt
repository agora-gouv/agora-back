package fr.social.gouv.agora.usecase.notification

import fr.social.gouv.agora.domain.Notification
import fr.social.gouv.agora.usecase.notification.repository.NotificationRepository
import org.springframework.stereotype.Service
import java.lang.Integer.min

@Service
class NotificationPaginatedUseCase(
    private val notificationRepository: NotificationRepository,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getNotificationPaginated(userId: String, pageNumber: Int): NotificationListAndHasMoreNotificationsFlag? {
        if (pageNumber <= 0) return null
        val notificationList = notificationRepository.getUserNotificationList(userId = userId)

        val minIndex = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (minIndex > notificationList.size) return null
        val maxIndex = min(pageNumber * MAX_PAGE_LIST_SIZE, notificationList.size)
        val hasMoreNotifications = maxIndex < notificationList.size

        val notifications = notificationList
            .sortedByDescending { notification -> notification.date }
            .subList(fromIndex = minIndex, toIndex = maxIndex)

        return NotificationListAndHasMoreNotificationsFlag(
            notifications = notifications,
            hasMoreNotifications = hasMoreNotifications,
        )
    }

}

data class NotificationListAndHasMoreNotificationsFlag(
    val notifications: List<Notification>,
    val hasMoreNotifications: Boolean,
)