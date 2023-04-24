package fr.social.gouv.agora.infrastructure.feedbackQag

import com.fasterxml.jackson.annotation.JsonProperty

data class FeedbackQagJson (
    @JsonProperty("isHelpful")
    val isHelpful: Boolean,
)