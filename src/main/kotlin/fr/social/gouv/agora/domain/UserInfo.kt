package fr.social.gouv.agora.domain

data class UserInfo(
    val userId: String,
    val fcmToken: String,
    val isBanned: Boolean,
    val authorizationList: List<UserAuthorization>,
)
