package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.domain.ChoiceResults
import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionResults
import fr.gouv.agora.domain.QuestionUniqueChoice
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class ConsultationResultJsonMapper {

    fun toJson(domain: ConsultationResults): ConsultationResultsJson {
        return ConsultationResultsJson(
            title = domain.consultation.title,
            coverUrl = domain.consultation.coverUrl,
            participantCount = domain.participantCount,
            resultsUniqueChoice = domain.resultsWithChoices.filter { result ->
                result.question is QuestionUniqueChoice || result.question is QuestionConditional
            }.map(::toQuestionResultsJson),
            resultsMultipleChoice = domain.resultsWithChoices.filter { result ->
                result.question is QuestionMultipleChoices
            }.map(::toQuestionResultsJson),
            resultsOpen = domain.openQuestions.map { questionOpen ->
                QuestionOpenResultsJson(
                    questionId = questionOpen.id,
                    questionTitle = questionOpen.title,
                    order = questionOpen.order,
                )
            },
        )
    }

    private fun toQuestionResultsJson(domain: QuestionResults): QuestionResultsJson {
        return QuestionResultsJson(
            questionId = domain.question.id,
            questionTitle = domain.question.title,
            order = domain.question.order,
            seenRatio = (domain.seenRatio * 100).roundToInt(),
            responses = domain.responses.map(::toChoiceResultsJson),
        )
    }

    private fun toChoiceResultsJson(domain: ChoiceResults): ChoiceResultsJson {
        return ChoiceResultsJson(
            choiceId = domain.choixPossible.id,
            label = domain.choixPossible.label,
            ratio = (domain.ratio * 100).roundToInt(),
        )
    }

}
