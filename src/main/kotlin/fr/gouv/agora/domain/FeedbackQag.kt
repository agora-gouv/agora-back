package fr.gouv.agora.domain

data class FeedbackQagInserting(
    val qagId: String,
    val userId: String,
    val isHelpful: Boolean,
)

data class FeedbackQag(
    val qagId: String,
    val userId: String,
    val isHelpful: Boolean,
)

data class FeedbackResults(
    val positiveRatio: Int,
    val negativeRatio: Int,
    val count: Int,
)