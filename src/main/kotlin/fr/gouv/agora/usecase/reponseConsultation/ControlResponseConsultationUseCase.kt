package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.QuestionMultipleChoices
import fr.social.gouv.agora.domain.QuestionOpen
import fr.social.gouv.agora.domain.QuestionUniqueChoice
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ControlResponseConsultationUseCase(private val questionRepository: QuestionRepository) {
    companion object {
        private const val MAX_TEXT_LENGTH = 400
    }

    fun isResponseConsultationValid(
        consultationId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): Boolean {
        for (response in consultationResponses) {
            val question = questionRepository.getConsultationQuestionList(consultationId)
                .find { question -> question.id == response.questionId }
            val listChoices = response.choiceIds
            when (question) {
                is QuestionMultipleChoices -> {
                    if (listChoices?.let { isChoiceIdDuplicated(it) } == true || listChoices?.let {
                            isChoiceIdListOverMaxChoices(
                                it,
                                question
                            )
                        } == true
                    ) return false
                }

                is QuestionUniqueChoice -> {
                    if (listChoices?.let { isChoiceIdOverOneForUniqueQuestion(it) } == true) return false
                }

                is QuestionOpen -> {
                    if (isTextFieldOverMaxLength(response)) return false
                }

                else -> return false
            }
        }
        return true
    }

    private fun isChoiceIdDuplicated(listChoices: List<String>): Boolean {
        return (listChoices.toSet().size < listChoices.size)
    }

    private fun isChoiceIdListOverMaxChoices(listChoices: List<String>, question: QuestionMultipleChoices): Boolean {
        return (question.maxChoices < listChoices.size)
    }

    private fun isChoiceIdOverOneForUniqueQuestion(listChoices: List<String>) = listChoices.size > 1
    private fun isTextFieldOverMaxLength(response: ReponseConsultationInserting) =
        response.responseText.length > MAX_TEXT_LENGTH
}
