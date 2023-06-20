package fr.social.gouv.agora.domain

sealed class ChoixPossible {
    abstract val id: String
    abstract val label: String
    abstract val ordre: Int
    abstract val questionId: String
}

data class ChoixPossibleDefault(
    override val id: String,
    override val label: String,
    override val ordre: Int,
    override val questionId: String,
) : ChoixPossible()

data class ChoixPossibleConditional(
    override val id: String,
    override val label: String,
    override val ordre: Int,
    override val questionId: String,
    val nextQuestionId: String,
) : ChoixPossible()