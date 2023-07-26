package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_APPLICABLE_CHOICE_UUID
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID_STRING
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.SKIP_QUESTION_CHOICE_UUID
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
                ) + createNotApplicableResponseChoice(
                    ordre = questionWithChoices.choixPossibleList.size + 2,
                    questionId = questionWithChoices.id,
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
                ) + createNotApplicableResponseChoice(
                    ordre = questionWithChoices.choixPossibleList.size + 2,
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
                choixPossibleList = questionWithChoices.choixPossibleList + createNotApplicableResponseConditionalChoice(
                    ordre = questionWithChoices.choixPossibleList.size + 1,
                    questionId = questionWithChoices.id,
                ),
            )

            else -> questionWithChoices
        }
    }

    private fun createNoResponseChoice(ordre: Int, questionId: String) = ChoixPossibleDefault(
        id = SKIP_QUESTION_CHOICE_UUID,
        label = "Pas de réponse",
        ordre = ordre,
        questionId = questionId,
    )

    private fun createNotApplicableResponseChoice(ordre: Int, questionId: String) = ChoixPossibleDefault(
        id = NOT_APPLICABLE_CHOICE_UUID,
        label = "Non applicable",
        ordre = ordre,
        questionId = questionId,
    )

    private fun createNotApplicableResponseConditionalChoice(ordre: Int, questionId: String) =
        createNotApplicableResponseChoice(ordre, questionId).let {
            ChoixPossibleConditional(
                id = it.id,
                label = it.label,
                ordre = it.ordre,
                questionId = it.questionId,
                nextQuestionId = NOT_FOUND_UUID_STRING,
            )
        }
}