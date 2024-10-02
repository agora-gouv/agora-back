package fr.gouv.agora.domain

import java.time.LocalDateTime

data class Concertation(
    val id: String,
    val title: String,
    val imageUrl: String,
    val externalLink: String,
    val thematique: Thematique,
    val updateLabel: String?,
    val updateDate: LocalDateTime,
    val territory: String = "National", // toutes les concertations sont nationales
)
