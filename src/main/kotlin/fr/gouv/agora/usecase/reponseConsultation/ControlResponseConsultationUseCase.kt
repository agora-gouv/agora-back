package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ControlResponseConsultationUseCase(
    private val questionRepository: QuestionRepository,
    private val controlQuestionMultipleChoicesUseCase: ControlQuestionMultipleChoicesUseCase,
    private val controlQuestionOpenUseCase: ControlQuestionOpenUseCase,
    private val communControlQuestionUseCase: CommunControlQuestionUseCase,
) {

    fun isResponseConsultationValid(
        consultationId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): Boolean {
        val questionList = questionRepository.getConsultationQuestionList(consultationId)
        val uniqueQuestionIdList =
            consultationResponses.map { consultationResponse -> consultationResponse.questionId }.toSet()
        if (uniqueQuestionIdList.size < consultationResponses.size)
            return false
        else {
            for (response in consultationResponses) {
                val question = questionList.find { question -> question.id == response.questionId }
                val listChoices = response.choiceIds
                when (question) {
                    is QuestionMultipleChoices -> {
                        if (listChoices?.let { controlQuestionMultipleChoicesUseCase.isChoiceIdDuplicated(it) } == true
                            || listChoices?.let {
                                controlQuestionMultipleChoicesUseCase.isChoiceIdListOverMaxChoices(it, question)
                            } == true
                            || communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)
                        ) return false
                    }

                    is QuestionUniqueChoice, is QuestionConditional -> {
                        if (listChoices?.let { communControlQuestionUseCase.isChoiceIdOverOne(it) } == true
                            || communControlQuestionUseCase.isChoiceAutreOverMaxLength(response)) return false
                    }

                    is QuestionOpen -> {
                        if (controlQuestionOpenUseCase.isTextFieldOverMaxLength(response)) return false
                    }

                    else -> return false
                }
            }
            return true
        }
    }
}


