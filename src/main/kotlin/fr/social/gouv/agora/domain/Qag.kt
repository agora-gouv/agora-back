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
    val supportCount: Int,
    val response: ResponseQag?,
)
