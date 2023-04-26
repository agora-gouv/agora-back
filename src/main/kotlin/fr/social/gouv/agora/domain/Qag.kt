package fr.social.gouv.agora.domain

import java.util.*

data class Qag(
    val id: String,
    val thematiqueId: String,
    val title: String,
    val description: String,
    val date: Date,
    val status: QagStatus,
    val username: String,
    val support: SupportQag?,
    val response: ResponseQag?,
    val feedback: FeedbackQagStatus?,
)
