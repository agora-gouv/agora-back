package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.*

abstract class ResponseQuestionValidator {
    companion object {
        private const val AUTRE_MAX_TEXT_LENGTH = 200
    }

    abstract fun isValid(question: Question, response: ReponseConsultationInserting): Boolean

    fun isChoiceIdOverOne(listChoices: List<String>) = listChoices.size > 1
    fun isChoiceAutreOverMaxLength(response: ReponseConsultationInserting) =
        response.responseText.length > AUTRE_MAX_TEXT_LENGTH

}

class QuestionMultipleChoicesValidator : ResponseQuestionValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return when (question) {
            is QuestionMultipleChoices -> (response.choiceIds?.let { isChoiceIdDuplicated(it) } == false && !isChoiceIdListOverMaxChoices(
                response.choiceIds, question
            )) && !isChoiceAutreOverMaxLength(response)

            else -> false
        }
    }

    private fun isChoiceIdDuplicated(listChoices: List<String>): Boolean {
        return (listChoices.toSet().size < listChoices.size)
    }

    private fun isChoiceIdListOverMaxChoices(listChoices: List<String>, question: QuestionMultipleChoices): Boolean {
        return (question.maxChoices < listChoices.size)
    }
}


class QuestionUniqueChoiceAndConditionalValidator : ResponseQuestionValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return when (question) {
            is QuestionUniqueChoice, is QuestionConditional -> (response.choiceIds?.let { isChoiceIdOverOne(it) } == false && !isChoiceAutreOverMaxLength(
                response
            ))

            else -> false
        }
    }
}


class QuestionOpenValidator : ResponseQuestionValidator() {

    companion object {
        private const val OPEN_QUESTION_MAX_TEXT_LENGTH = 400
    }

    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return when (question) {
            is QuestionOpen -> !isTextFieldOverMaxLength(response)
            else -> false
        }
    }

    private fun isTextFieldOverMaxLength(response: ReponseConsultationInserting) =
        response.responseText.length > OPEN_QUESTION_MAX_TEXT_LENGTH
}

