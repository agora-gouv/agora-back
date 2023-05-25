package fr.social.gouv.agora.infrastructure.featureFlags

import com.fasterxml.jackson.annotation.JsonProperty

data class FeatureFlagsJson(
    @JsonProperty("askQag")
    val askQag: Boolean,
)
