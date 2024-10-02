package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

data class ConcertationJson(
    val id: String,
    val title: String,
    val imageUrl: String,
    val externalLink: String,
    val thematique: ThematiqueNoIdJson,
    val updateLabel: String?,
    val updateDate: String,
    val territory: String,
)
