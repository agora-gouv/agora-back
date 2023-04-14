package fr.social.gouv.agora.domain

data class Question(
    val id: String,
    val label: String,
    val ordre: Int,
    val type: String,
    val maxChoices: Int?,
    val consultationId: String,
    val choixPossibleList: List<ChoixPossible>,
)
