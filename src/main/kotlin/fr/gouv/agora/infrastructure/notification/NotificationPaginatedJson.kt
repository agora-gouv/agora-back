package fr.gouv.agora.infrastructure.notification

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationPaginatedJson(
    @JsonProperty("hasMoreNotifications")
    val hasMoreNotifications: Boolean,
    @JsonProperty("notifications")
    val notifications: List<NotificationJson>,
)

data class NotificationJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("date")
    val date: String,
)
