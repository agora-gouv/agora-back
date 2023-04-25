package fr.social.gouv.agora.domain

data class FeedbackQagInserting(
    val userId: String,
    val qagId: String,
    val isHelpful: Boolean,
)

data class FeedbackQag(
    val userId: String,
    val qagId: String,
)

data class FeedbackQagStatus(
    val isExist: Boolean,
)