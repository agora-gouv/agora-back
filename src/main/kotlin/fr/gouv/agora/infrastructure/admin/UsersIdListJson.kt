package fr.gouv.agora.infrastructure.admin

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class UsersIdListJson(
    @JsonProperty("usersID") @Schema(example = "[\"a8480034-d383-4b43-8fd7-3df10d3ce41f\"]")
    val usersID: List<String>,
)
