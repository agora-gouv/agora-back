package fr.social.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonProperty

data class SignupInfoJson(
    @JsonProperty("userId")
    val userId: String,
    @JsonProperty("jwtToken")
    val jwtToken: String,
    @JsonProperty("loginToken")
    val loginToken: String,
    @JsonProperty("isModerator")
    val isModerator: Boolean = false,
)