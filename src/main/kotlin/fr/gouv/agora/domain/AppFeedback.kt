package fr.gouv.agora.domain

data class AppFeedbackInserting(
    val userId: String,
    val type: AppFeedbackType,
    val description: String,
    val deviceInfo: AppFeedbackDeviceInfo?,
)

enum class AppFeedbackType {
    BUG, FEATURE_REQUEST, COMMENT
}

data class AppFeedbackDeviceInfo(
    val model: String,
    val osVersion: String,
    val appVersion: String,
)