package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.QuestionMultipleChoices
import org.springframework.stereotype.Service

@Service
class ControlQuestionMultipleChoicesUseCase {

    fun isChoiceIdDuplicated(listChoices: List<String>): Boolean {
        return (listChoices.toSet().size < listChoices.size)
    }

    fun isChoiceIdListOverMaxChoices(listChoices: List<String>, question: QuestionMultipleChoices): Boolean {
        return (question.maxChoices < listChoices.size)
    }
}


