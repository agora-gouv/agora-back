package fr.social.gouv.agora.domain

data class ChoixPossible(
    val id: String,
    val label: String,
    val ordre: Int,
    val questionId: String,
)
