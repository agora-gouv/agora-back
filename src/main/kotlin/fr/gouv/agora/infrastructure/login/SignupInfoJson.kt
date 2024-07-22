package fr.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Model for sign up info")
data class SignupInfoJson(
    @field:Schema(
        description = "An uniq id for user",
        example = "61c9ae56-8d75-4973-941e-4494b3195c47",
        type = "String",
    )
    @JsonProperty("userId")
    val userId: String,
    @JsonProperty("jwtToken")
    val jwtToken: String,
    @JsonProperty("jwtExpirationEpochMilli")
    val jwtExpirationEpochMilli: Long,
    @JsonProperty("loginToken")
    val loginToken: String,
    @JsonProperty("isModerator")
    val isModerator: Boolean = false,
)
