package fr.gouv.agora.domain

data class LoginRequest(
    val userId: String,
    val ipAddressHash: String,
    val userAgent: String,
    val fcmToken: String,
    val platform: String,
    val versionName: String,
    val versionCode: String,
)