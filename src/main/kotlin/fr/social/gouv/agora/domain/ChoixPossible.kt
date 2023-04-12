package fr.social.gouv.agora.domain

data class ChoixPossible(
    val id: String,
    val label: String,
    val ordre: Int,
    val id_question: String,
)
