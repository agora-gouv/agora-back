package fr.gouv.agora.domain

data class Questions(
    val questionCount: Int,
    val questions: List<Question>,
)

sealed class Question {
    abstract val id: String
    abstract val title: String
    abstract val popupDescription: String?
    abstract val order: Int
    abstract val nextQuestionId: String?
    abstract val consultationId: String
}

abstract class QuestionWithChoices : Question() {
    abstract val choixPossibleList: List<ChoixPossible>
}

data class QuestionUniqueChoice(
    override val id: String,
    override val title: String,
    override val popupDescription: String?,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    override val choixPossibleList: List<ChoixPossibleDefault>,
) : QuestionWithChoices()

data class QuestionMultipleChoices(
    override val id: String,
    override val title: String,
    override val popupDescription: String?,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    override val choixPossibleList: List<ChoixPossibleDefault>,
    val maxChoices: Int,
) : QuestionWithChoices()

data class QuestionConditional(
    override val id: String,
    override val title: String,
    override val popupDescription: String?,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    override val choixPossibleList: List<ChoixPossibleConditional>,
) : QuestionWithChoices()

data class QuestionOpen(
    override val id: String,
    override val title: String,
    override val popupDescription: String?,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
) : Question()

data class QuestionChapter(
    override val id: String,
    override val title: String,
    override val popupDescription: String?,
    override val order: Int,
    override val nextQuestionId: String?,
    override val consultationId: String,
    val urlImage: String?,
    val description: String,
) : Question()
