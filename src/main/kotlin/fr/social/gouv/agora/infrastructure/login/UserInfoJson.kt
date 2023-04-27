package fr.social.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInfoJson(
    @JsonProperty("userId")
    val userId: String,
)