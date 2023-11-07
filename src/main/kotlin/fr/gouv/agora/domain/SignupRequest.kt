package fr.gouv.agora.domain

data class SignupRequest(
    val remoteAddress: String,
    val userAgent: String,
    val fcmToken: String,
    val platform: String,
    val versionName: String,
    val versionCode: String,
)