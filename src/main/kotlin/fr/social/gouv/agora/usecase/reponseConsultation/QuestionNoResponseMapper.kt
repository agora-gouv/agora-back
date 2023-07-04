package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID_STRING
import org.springframework.stereotype.Component

@Component
class QuestionNoResponseMapper {

    @Suppress("UNCHECKED_CAST")
    fun toQuestionNoResponse(questionWithChoices: QuestionWithChoices): QuestionWithChoices {
        val ordre = questionWithChoices.choixPossibleList.size + 1
        val modifiedChoicesList =
            questionWithChoices.choixPossibleList as List<ChoixPossibleDefault> + createNoResponseChoice(
                ordre,
                questionWithChoices.id
            )
        return when (questionWithChoices) {
            is QuestionUniqueChoice -> QuestionUniqueChoice(
                id = questionWithChoices.id,
                title = questionWithChoices.title,
                popupDescription = questionWithChoices.popupDescription,
                order = ordre,
                nextQuestionId = questionWithChoices.nextQuestionId,
                consultationId = questionWithChoices.consultationId,
                choixPossibleList = modifiedChoicesList,
            )

            is QuestionMultipleChoices -> QuestionMultipleChoices(
                id = questionWithChoices.id,
                title = questionWithChoices.title,
                popupDescription = questionWithChoices.popupDescription,
                order = ordre,
                nextQuestionId = questionWithChoices.nextQuestionId,
                consultationId = questionWithChoices.consultationId,
                choixPossibleList = modifiedChoicesList,
                maxChoices = questionWithChoices.maxChoices
            )

            else -> questionWithChoices
        }
    }

    private fun createNoResponseChoice(ordre: Int, questionId: String) = ChoixPossibleDefault(
        id = NOT_FOUND_UUID_STRING,
        label = "Pas de réponse",
        ordre = ordre,
        questionId = questionId,
    )
}