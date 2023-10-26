package fr.gouv.agora.domain

import java.util.*

data class Notification(
    val title: String,
    val description: String,
    val type: NotificationType,
    val date: Date,
    val userId: String,
)

data class NotificationInserting(
    val title: String,
    val description: String,
    val type: NotificationType,
    val userIds: List<String>,
)

enum class NotificationType {
    CONSULTATION,
    QAG,
}
