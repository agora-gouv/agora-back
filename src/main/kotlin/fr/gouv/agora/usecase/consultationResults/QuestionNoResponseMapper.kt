package fr.gouv.agora.usecase.consultationResults

import fr.gouv.agora.domain.ChoixPossibleDefault
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.domain.QuestionWithChoices
import fr.gouv.agora.infrastructure.utils.UuidUtils.SKIP_QUESTION_CHOICE_UUID
import org.springframework.stereotype.Component

@Component
class QuestionNoResponseMapper {

    fun toQuestionNoResponse(questionWithChoices: QuestionWithChoices): QuestionWithChoices {
        return when (questionWithChoices) {
            is QuestionUniqueChoice -> QuestionUniqueChoice(
                id = questionWithChoices.id,
                title = questionWithChoices.title,
                popupDescription = questionWithChoices.popupDescription,
                order = questionWithChoices.order,
                nextQuestionId = questionWithChoices.nextQuestionId,
                consultationId = questionWithChoices.consultationId,
                choixPossibleList = questionWithChoices.choixPossibleList + createNoResponseChoice(
                    ordre = questionWithChoices.choixPossibleList.size + 1,
                    questionId = questionWithChoices.id
                ),
            )

            is QuestionMultipleChoices -> QuestionMultipleChoices(
                id = questionWithChoices.id,
                title = questionWithChoices.title,
                popupDescription = questionWithChoices.popupDescription,
                order = questionWithChoices.order,
                nextQuestionId = questionWithChoices.nextQuestionId,
                consultationId = questionWithChoices.consultationId,
                choixPossibleList = questionWithChoices.choixPossibleList + createNoResponseChoice(
                    ordre = questionWithChoices.choixPossibleList.size + 1,
                    questionId = questionWithChoices.id,
                ),
                maxChoices = questionWithChoices.maxChoices
            )

            is QuestionConditional -> QuestionConditional(
                id = questionWithChoices.id,
                title = questionWithChoices.title,
                popupDescription = questionWithChoices.popupDescription,
                order = questionWithChoices.order,
                nextQuestionId = questionWithChoices.nextQuestionId,
                consultationId = questionWithChoices.consultationId,
                choixPossibleList = questionWithChoices.choixPossibleList,
            )

            else -> questionWithChoices
        }
    }

    private fun createNoResponseChoice(ordre: Int, questionId: String) = ChoixPossibleDefault(
        id = SKIP_QUESTION_CHOICE_UUID,
        label = "Pas de réponse",
        ordre = ordre,
        questionId = questionId,
        hasOpenTextField = false,
    )

}