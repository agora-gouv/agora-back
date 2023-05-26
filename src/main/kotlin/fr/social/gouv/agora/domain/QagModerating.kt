package fr.social.gouv.agora.domain

import java.util.*

data class QagModerating(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val description: String,
    val username: String,
    val date: Date,
    val support: SupportQag?,
)
