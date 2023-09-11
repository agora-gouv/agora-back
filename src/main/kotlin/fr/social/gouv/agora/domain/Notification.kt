package fr.social.gouv.agora.domain

import java.util.*

data class Notification(
    val id: String,
    val title: String,
    val type: NotificationType,
    val date: Date,
    val userId: String,
)

enum class NotificationType {
    CONSULTATION,
    QAG,
}
