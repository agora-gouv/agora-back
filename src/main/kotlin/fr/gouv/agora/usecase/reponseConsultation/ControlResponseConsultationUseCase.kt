package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.question.repository.QuestionRepository
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

        return consultationResponses.all { response ->
            val question = questionList.find { it.id == response.questionId }
            val listChoices = response.choiceIds
            val responseQuestionValidator = getResponseQuestionValidator(question, listChoices)
            question?.let { responseQuestionValidator?.isValid(question, response) } == true
        }
}
    private fun getResponseQuestionValidator(
        question: Question?,
        listChoices: List<String>?
    ): ResponseQuestionValidator? {
        return when (question) {
            is QuestionMultipleChoices -> listChoices?.let {
                QuestionMultipleChoicesValidator(
                    controlQuestionMultipleChoicesUseCase,
                    communControlQuestionUseCase,
                    it
                )
            }

            is QuestionUniqueChoice, is QuestionConditional -> listChoices?.let {
                QuestionUniqueChoiceAndConditionalValidator(
                    communControlQuestionUseCase,
                    it
                )
            }

            is QuestionOpen -> QuestionOpenValidator(controlQuestionOpenUseCase)

            else -> null
        }
    }
}



