package fr.social.gouv.agora.domain

data class FeedbackQag(
    val userId: String,
    val qagId: String,
    val isHelpful: Boolean,
)