package fr.gouv.agora.infrastructure.feedbackConsultationUpdate

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class FeedbackConsultationUpdateResultsJson(
    @JsonProperty("userResponse")
    val userResponse: Boolean,
    @JsonProperty("stats")
    val stats: FeedbackConsultationUpdateStatsJson?,
)

data class FeedbackConsultationUpdateStatsJson(
    @JsonProperty("positiveRatio")
    val positiveRatio: Int,
    @JsonProperty("negativeRatio")
    val negativeRatio: Int,
    @JsonProperty("responseCount")
    val responseCount: Int,
)

data class InsertFeedbackConsultationUpdateJson(
    @JsonProperty("isPositive")
    val isPositive: Boolean,
)
