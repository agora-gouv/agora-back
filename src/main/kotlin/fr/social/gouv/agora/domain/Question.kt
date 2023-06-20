package fr.social.gouv.agora.domain

sealed class Question {
    abstract val id: String
    abstract val title: String
    abstract val order: Int
    abstract val nextQuestionId: String?
    abstract val consultationId: String
}

abstract class QuestionWithChoices : Question() {
    abstract val choixPossibleList: List<ChoixPossible>
}

data class QuestionChoixUnique(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    override val choixPossibleList: List<ChoixPossibleDefault>,
) : QuestionWithChoices()

data class QuestionChoixMultiple(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    override val choixPossibleList: List<ChoixPossibleDefault>,
    val maxChoices: Int,
) : QuestionWithChoices()

data class QuestionOuverte(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
) : Question()

data class Chapitre(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    val description: String,
) : Question()

data class QuestionConditional(
    override val id: String,
    override val title: String,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    override val choixPossibleList: List<ChoixPossibleConditional>,
) : QuestionWithChoices()