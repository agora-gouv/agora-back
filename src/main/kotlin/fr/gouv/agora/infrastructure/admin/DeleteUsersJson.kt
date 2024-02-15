package fr.gouv.agora.infrastructure.admin

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteUsersJson(
    @JsonProperty("userIDsToDelete")
    val userIDsToDelete: List<String>,
)
