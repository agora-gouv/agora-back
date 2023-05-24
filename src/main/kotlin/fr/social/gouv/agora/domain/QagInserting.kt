package fr.social.gouv.agora.domain

import java.util.*

data class QagInserting(
    val thematiqueId: String,
    val title: String,
    val description: String,
    val date: Date,
    val status: QagStatus,
    val username: String,
    val userId: String,
)
