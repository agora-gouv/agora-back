package fr.gouv.agora.infrastructure.profile

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ProfileDepartmentJson(
    @JsonProperty("departments") @Schema(example = "[\"Paris\"]")
    val departments: List<String>
)
