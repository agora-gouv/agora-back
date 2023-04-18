package fr.social.gouv.agora.infrastructure.reponseConsultation.dto

import fr.social.gouv.agora.domain.ChoiceResult
import fr.social.gouv.agora.domain.ConsultationResult
import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.QuestionResult
import fr.social.gouv.agora.infrastructure.reponseConsultation.ChoiceResultJson
import fr.social.gouv.agora.infrastructure.reponseConsultation.ConsultationResultJson
import fr.social.gouv.agora.infrastructure.reponseConsultation.ConsultationUpdatesJson
import fr.social.gouv.agora.infrastructure.reponseConsultation.QuestionResultJson
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class ConsultationResultJsonMapper {

    fun toJson(domain: ConsultationResult): ConsultationResultJson {
        return ConsultationResultJson(
            title = domain.consultation.title,
            participantCount = domain.participantCount,
            results = domain.results.map(::toQuestionResultJson),
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