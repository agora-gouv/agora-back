package fr.gouv.agora.domain

import java.time.LocalDate

data class FicheInventaire(
    val etapeLancement: String,
    val etapeAnalyse: String,
    val etapeSuivi: String,
    val titre: String,
    val debut: LocalDate,
    val fin: LocalDate,
    val porteur: String,
    val lienSite: String,
    val conditionParticipation: String,
    val modaliteParticipation: String,
    val objectif: String,
    val thematique: Thematique,
)
