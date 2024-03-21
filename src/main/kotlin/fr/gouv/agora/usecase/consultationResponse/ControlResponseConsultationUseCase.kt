package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ControlResponseConsultationUseCase(
    private val questionRepository: QuestionRepository,
    private val questionMultipleChoicesValidator: QuestionMultipleChoicesValidator,
    private val questionUniqueChoiceAndConditionalValidator: QuestionUniqueChoiceAndConditionalValidator,
    private val questionOpenValidator: QuestionOpenValidator,
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
            questionList.find { it.id == response.questionId }?.let { question ->
                getResponseQuestionValidator(question)?.isValid(question, response) ?: true
            } ?: false
        }
    }

    private fun getResponseQuestionValidator(
        question: Question,
    ): ResponseQuestionValidator? {
        return when (question) {
            is QuestionMultipleChoices -> questionMultipleChoicesValidator
            is QuestionUniqueChoice -> questionUniqueChoiceAndConditionalValidator
            is QuestionConditional -> questionUniqueChoiceAndConditionalValidator
            is QuestionWithChoices -> null
            is QuestionOpen -> questionOpenValidator
            is QuestionChapter -> null
        }
    }
}



