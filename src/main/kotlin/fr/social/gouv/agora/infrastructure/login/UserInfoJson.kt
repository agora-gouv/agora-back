package fr.social.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInfoJson(
    @JsonProperty("jwtToken")
    val jwtToken: String,
    @JsonProperty("loginToken")
    val loginToken: String,
)