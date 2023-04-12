package fr.social.gouv.agora.domain

import fr.social.gouv.agora.infrastructure.choixpossible.dto.ChoixPossibleDTO

data class Question(
    val id: String,
    val label: String,
    val ordre: Int,
    val type: String,
    val id_consultation: String,
    val listechoix: List<ChoixPossible>,
)
