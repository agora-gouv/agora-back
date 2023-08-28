package fr.social.gouv.agora.domain

sealed class ChoixPossible {
    abstract val id: String
    abstract val label: String
    abstract val ordre: Int
    abstract val questionId: String
    abstract val hasOpenTextField: Boolean
}

data class ChoixPossibleDefault(
    override val id: String,
    override val label: String,
    override val ordre: Int,
    override val questionId: String,
    override val hasOpenTextField: Boolean,
) : ChoixPossible()

data class ChoixPossibleConditional(
    override val id: String,
    override val label: String,
    override val ordre: Int,
    override val questionId: String,
    override val hasOpenTextField: Boolean,
    val nextQuestionId: String,
) : ChoixPossible()