package fr.social.gouv.agora.infrastructure.notification

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NotificationPaginatedJson(
    @JsonProperty("hasMoreNotifications")
    val hasMoreNotifications: Boolean,
    @JsonProperty("notifications")
    val notifications: List<NotificationJson>,
)

data class NotificationJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("date")
    val date: String,
)