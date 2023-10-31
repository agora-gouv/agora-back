package fr.gouv.agora.domain

import java.util.*

data class ResponseQagPreview(
    val qagId: String,
    val thematique: Thematique,
    val title: String,
    val author: String,
    val authorPortraitUrl: String,
    val responseDate: Date,
)