package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.domain.ChoiceResults
import fr.gouv.agora.domain.Conclusion
import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.domain.ConsultationResultsWithUpdate
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.Explanation
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionResults
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.domain.Video
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class ConsultationResultJsonMapper(private val dateMapper: DateMapper) {

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
                )
            },
        )
    }

    fun toJson(domain: ConsultationResultsWithUpdate): ConsultationResultsWithUpdateJson {
        return ConsultationResultsWithUpdateJson(
            title = domain.consultation.title,
            participantCount = domain.participantCount,
            resultsUniqueChoice = domain.results.filter { result ->
                result.question is QuestionUniqueChoice || result.question is QuestionConditional
            }.map(::toQuestionResultsJson),
            resultsMultipleChoice = domain.results.filter { result ->
                result.question is QuestionMultipleChoices
            }.map(::toQuestionResultsJson),
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
            ),
            presentation = PresentationJson(
                startDate = dateMapper.toFormattedDate(domain.consultation.startDate),
                endDate = dateMapper.toFormattedDate(domain.consultation.endDate),
                description = domain.consultation.description,
                tipsDescription = domain.consultation.tipsDescription,
            )
        )
    }

    private fun toQuestionResultsJson(domain: QuestionResults): QuestionResultsJson {
        return QuestionResultsJson(
            questionId = domain.question.id,
            questionTitle = domain.question.title,
            order = domain.question.order,
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

    private fun toExplanationJson(domain: Explanation): ExplanationJson {
        return ExplanationJson(
            isTogglable = domain.isTogglable,
            title = domain.title,
            intro = domain.intro,
            imageUrl = domain.image?.url ?: System.getenv("DEFAULT_EXPLANATION_IMAGE_URL"),
            image = domain.image?.let { image ->
                ImageJson(url = image.url, description = image.description)
            },
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
