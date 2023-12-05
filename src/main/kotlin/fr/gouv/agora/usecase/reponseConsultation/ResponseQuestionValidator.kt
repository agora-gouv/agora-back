package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.ReponseConsultationInserting

abstract class ResponseQuestionValidator {
    abstract fun isValid(question: Question, response: ReponseConsultationInserting): Boolean
}

class QuestionMultipleChoicesValidator(
    private val controlQuestionMultipleChoicesUseCase: ControlQuestionMultipleChoicesUseCase,
    private val communControlQuestionUseCase: CommunControlQuestionUseCase,
    private val listChoices: List<String>,
) : ResponseQuestionValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return !(controlQuestionMultipleChoicesUseCase.isChoiceIdDuplicated(listChoices)
                || controlQuestionMultipleChoicesUseCase.isChoiceIdListOverMaxChoices(listChoices, question as QuestionMultipleChoices)
                || communControlQuestionUseCase.isChoiceAutreOverMaxLength(response))
    }
}


class QuestionUniqueChoiceAndConditionalValidator(
    private val communControlQuestionUseCase: CommunControlQuestionUseCase,
    private val listChoices: List<String>,
) : ResponseQuestionValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return !(communControlQuestionUseCase.isChoiceIdOverOne(listChoices) || communControlQuestionUseCase.isChoiceAutreOverMaxLength(
            response
        ))
    }
}


class QuestionOpenValidator(
    private val controlQuestionOpenUseCase: ControlQuestionOpenUseCase,
) : ResponseQuestionValidator() {
    override fun isValid(question: Question, response: ReponseConsultationInserting): Boolean {
        return !controlQuestionOpenUseCase.isTextFieldOverMaxLength(response)
    }
}

