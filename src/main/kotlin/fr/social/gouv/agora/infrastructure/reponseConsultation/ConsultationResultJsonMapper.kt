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
                result.question is QuestionUniqueChoice || result.question is QuestionConditional
            }.map(::toQuestionResultJson),
            resultsMultipleChoice = domain.results.filter { result ->
                result.question is QuestionMultipleChoices
            }.map(::toQuestionResultJson),
            lastUpdate = ConsultationUpdatesJson(
                step = when (domain.lastUpdate.status) {
                    ConsultationStatus.COLLECTING_DATA -> 1
                    ConsultationStatus.POLITICAL_COMMITMENT -> 2
                    ConsultationStatus.EXECUTION -> 3
                },
                description = domain.lastUpdate.description,
                explanationsTitle = domain.lastUpdate.explanationsTitle,
                explanations = domain.lastUpdate.explanations?.map(::toExplanationJson),
                video = domain.lastUpdate.video?.let { video -> toVideoJson(video) },
                conclusion = domain.lastUpdate.conclusion?.let { conclusion -> toConclusionJson(conclusion) },
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

    private fun toExplanationJson(domain: Explanation): ExplanationJson {
        return ExplanationJson(
            isTogglable = domain.isTogglable,
            title = domain.title,
            intro = domain.intro,
            imageUrl = domain.imageUrl,
            description = domain.description,
        )
    }

    private fun toVideoJson(domain: Video): VideoJson {
        return VideoJson(
            title = domain.title,
            intro = domain.intro,
            videoUrl = domain.url,
            videoWidth = domain.width,
            videoHeight = domain.height,
            transcription = domain.transcription,
        )
    }

    private fun toConclusionJson(domain: Conclusion): ConclusionJson {
        return ConclusionJson(
            title = domain.title,
            description = domain.description,
        )
    }
}
