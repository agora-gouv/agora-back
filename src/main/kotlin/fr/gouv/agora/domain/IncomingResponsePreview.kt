package fr.gouv.agora.domain

import java.time.LocalDate

data class IncomingResponsePreview(
    val id: String,
    val thematique: Thematique,
    val title: String,
    val supportCount: Int,
    val dateLundiPrecedent: LocalDate,
    val dateLundiSuivant: LocalDate,
    val order: Int,
)
