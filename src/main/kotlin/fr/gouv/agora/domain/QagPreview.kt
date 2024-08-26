package fr.gouv.agora.domain

import java.util.*

data class QagPreview(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val description: String,
    val username: String,
    val date: Date,
    val supportCount: Int,
    val isSupportedByUser: Boolean,
    val isAuthor: Boolean,
)
