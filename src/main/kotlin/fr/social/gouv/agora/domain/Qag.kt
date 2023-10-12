package fr.social.gouv.agora.domain

import java.util.*

data class Qag(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val description: String,
    val date: Date,
    val status: QagStatus,
    val username: String,
    val userId: String,
    val support: SupportQag,
    val response: ResponseQag?,
    val feedback: Boolean,
    val feedbackResults: List<FeedbackQag>,
)
