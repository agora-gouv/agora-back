package fr.social.gouv.agora.usecase.qag.repository

import java.util.*

data class QagModeratingInfo(
    val id: String,
    val thematiqueId: String,
    val title: String,
    val description: String,
    val username: String,
    val date: Date,
)
