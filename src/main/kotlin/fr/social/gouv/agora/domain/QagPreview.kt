package fr.social.gouv.agora.domain

import java.util.*

data class QagPreview(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val username: String,
    val date: Date,
    val support: SupportQag,
    val isAuthor: Boolean,
)