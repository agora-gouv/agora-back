package fr.gouv.agora.infrastructure.participationCharter

import com.fasterxml.jackson.annotation.JsonProperty

data class ParticipationCharterJson(
    @JsonProperty("extraText")
    val extraText: String,
)