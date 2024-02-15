package fr.gouv.agora.infrastructure.appFeedback

import com.fasterxml.jackson.annotation.JsonProperty

data class AppFeedbackJson(
    @JsonProperty("type")
    val type: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("deviceInfo")
    val deviceInfo: AppFeedbackDeviceInfoJson?,
)

data class AppFeedbackDeviceInfoJson(
    @JsonProperty("model")
    val model: String,
    @JsonProperty("osVersion")
    val osVersion: String,
    @JsonProperty("appVersion")
    val appVersion: String,
)