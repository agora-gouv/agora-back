package fr.social.gouv.agora.domain

sealed class Question {
    abstract val id: String
    abstract val title: String
    abstract val order: Int
    abstract val consultationId: String
}

data class QuestionChoixUnique(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val consultationId: String,
    val choixPossibleList: List<ChoixPossible>,
) : Question()

data class QuestionChoixMultiple(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val consultationId: String,
    val choixPossibleList: List<ChoixPossible>,
    val maxChoices: Int,
) : Question()

data class QuestionOpened(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val consultationId: String,
) : Question()

data class Chapter(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val consultationId: String,
    val description: String,
) : Question()

