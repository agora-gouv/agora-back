package fr.social.gouv.agora.domain

import java.util.Date

data class ModeratusQag(
    val qagId: String,
    val title: String,
    val description: String,
    val date: Date,
    val username: String,
    val userId: String,
)