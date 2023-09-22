package fr.social.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginInfoJson(
    @JsonProperty("jwtToken")
    val jwtToken: String,
    @JsonProperty("isModerator")
    val isModerator: Boolean = false,
)