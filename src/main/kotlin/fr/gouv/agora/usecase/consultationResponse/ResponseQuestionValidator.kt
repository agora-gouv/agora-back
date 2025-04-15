package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.domain.ReponseConsultationInserting
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
        if (question !is QuestionMultipleChoices || response.choiceIds == null) {
            return false
        }

        val isChoiceIdListUnderMaxChoices = (response.choiceIds.size <= question.maxChoices)
        val isChoiceIdListUnique = (response.choiceIds.toSet().size == response.choiceIds.size)

        return isChoiceIdListUnique
                && isChoiceIdListUnderMaxChoices
                && isValidOpenTextFieldLength(response)
    }
}

@Component
class QuestionUniqueChoiceAndConditionalValidator : ResponseQuestionWithChoicesValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        if (question !is QuestionUniqueChoice && question !is QuestionConditional) {
            return false
        }
        if (response.choiceIds == null) return false

        val hasOneChoiceId = response.choiceIds.size == 1

        return hasOneChoiceId && isValidOpenTextFieldLength(response)
    }
}

@Component
class QuestionOpenValidator : ResponseQuestionValidator() {
    companion object {
        private const val OPEN_QUESTION_MAX_TEXT_LENGTH = 400
    }

    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        if (question !is QuestionOpen) return false

        val isValidOpenTextFieldLength = response.responseText.length <= OPEN_QUESTION_MAX_TEXT_LENGTH

        return isValidOpenTextFieldLength
    }
}
