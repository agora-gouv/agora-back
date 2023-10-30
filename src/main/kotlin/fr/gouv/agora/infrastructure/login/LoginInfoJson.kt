package fr.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginInfoJson(
    @JsonProperty("jwtToken")
    val jwtToken: String,
    @JsonProperty("jwtExpirationEpochMilli")
    val jwtExpirationEpochMilli: Long,
    @JsonProperty("isModerator")
    val isModerator: Boolean = false,
)