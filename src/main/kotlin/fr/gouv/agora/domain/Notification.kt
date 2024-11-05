package fr.gouv.agora.domain

import fr.gouv.agora.infrastructure.notification.TypeNotification
import java.util.*

data class Notification(
    val title: String,
    val description: String,
    val type: TypeNotification,
    val date: Date,
    val userId: String,
)

data class NotificationInserting(
    val title: String,
    val description: String,
    val type: TypeNotification,
    val userIds: List<String>,
)
