package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.domain.*
import org.springframework.stereotype.Component

abstract class ResponseQuestionValidator {
    abstract fun isValid(question: Question, response: ReponseConsultationInserting): Boolean
}

abstract class ResponseQuestionWithChoicesValidator : ResponseQuestionValidator() {
    companion object {
        private const val OPEN_FIELD_TEXT_MAX_LENGTH = 200
    }

    fun isValidOpenTextFieldLength(response: ReponseConsultationInserting) =
        response.responseText.length <= OPEN_FIELD_TEXT_MAX_LENGTH

}
@Component
class QuestionMultipleChoicesValidator : ResponseQuestionWithChoicesValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return if (question is QuestionMultipleChoices)
            response.choiceIds?.let {
                isChoiceIdListUnique(it)
                        && isChoiceIdListUnderMaxChoices(it, question)
                        && isValidOpenTextFieldLength(response)
            } == true
        else false
    }

    private fun isChoiceIdListUnique(listChoices: List<String>): Boolean {
        return (listChoices.toSet().size == listChoices.size)
    }

    private fun isChoiceIdListUnderMaxChoices(listChoices: List<String>, question: QuestionMultipleChoices): Boolean {
        return (listChoices.size <= question.maxChoices)
    }
}

@Component
class QuestionUniqueChoiceAndConditionalValidator : ResponseQuestionWithChoicesValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return if (question is QuestionUniqueChoice || question is QuestionConditional)
            response.choiceIds?.let { hasOneChoiceId(it) && isValidOpenTextFieldLength(response) } == true
        else false
    }

    private fun hasOneChoiceId(listChoices: List<String>) = listChoices.size == 1
}

@Component
class QuestionOpenValidator : ResponseQuestionValidator() {

    companion object {
        private const val OPEN_QUESTION_MAX_TEXT_LENGTH = 400
    }

    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return if (question is QuestionOpen) isValidOpenTextFieldLength(response) else false
    }

    private fun isValidOpenTextFieldLength(response: ReponseConsultationInserting) =
        response.responseText.length <= OPEN_QUESTION_MAX_TEXT_LENGTH
}

