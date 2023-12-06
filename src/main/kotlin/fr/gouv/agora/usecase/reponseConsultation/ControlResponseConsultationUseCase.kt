package fr.gouv.agora.usecase.reponseConsultation

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
            val question = questionList.find { it.id == response.questionId }
            val responseQuestionValidator = question?.let { getResponseQuestionValidator(it) }
            question?.let { responseQuestionValidator?.isValid(question, response) } == true
        }
    }

    private fun getResponseQuestionValidator(
        question: Question,
    ): ResponseQuestionValidator? {
        return when (question) {
            is QuestionOpen -> questionOpenValidator
            is QuestionChapter -> null
            is QuestionWithChoices ->
                if (question is QuestionMultipleChoices) questionMultipleChoicesValidator
                else questionUniqueChoiceAndConditionalValidator
        }
    }
}



