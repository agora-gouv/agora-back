package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class ConsultationResultJsonMapper {

    fun toJson(domain: ConsultationResult): ConsultationResultJson {
        return ConsultationResultJson(
            title = domain.consultation.title,
            participantCount = domain.participantCount,
            resultsUniqueChoice = domain.results.filter { result ->
                result.question is QuestionChoixUnique
            }.map(::toQuestionResultJson),
            resultsMultipleChoice = domain.results.filter { result ->
                result.question is QuestionChoixMultiple
            }.map(::toQuestionResultJson),
            lastUpdate = ConsultationUpdatesJson(
                step = when (domain.lastUpdate.status) {
                    ConsultationStatus.COLLECTING_DATA -> 1
                    ConsultationStatus.POLITICAL_COMMITMENT -> 2
                    ConsultationStatus.EXECUTION -> 3
                },
                description = domain.lastUpdate.description,
            )
        )
    }

    private fun toQuestionResultJson(domain: QuestionResult): QuestionResultJson {
        return QuestionResultJson(
            questionTitle = domain.question.title,
            order = domain.question.order,
            responses = domain.responses.map(::toChoiceResultJson),
        )
    }

    private fun toChoiceResultJson(domain: ChoiceResult): ChoiceResultJson {
        return ChoiceResultJson(
            label = domain.choixPossible.label,
            ratio = (domain.ratio * 100).roundToInt(),
        )
    }

}