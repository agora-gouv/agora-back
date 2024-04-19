package fr.gouv.agora.usecase.consultationResults

import fr.gouv.agora.domain.ChoiceResults
import fr.gouv.agora.domain.ChoixPossible
import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionResults
import fr.gouv.agora.domain.QuestionWithChoices
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheResult
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class ConsultationResultsUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val consultationResultAggregatedRepository: ConsultationResultAggregatedRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val mapper: QuestionNoResponseMapper,
    private val cacheRepository: ConsultationResultsCacheRepository,
) {

    fun getConsultationResults(consultationId: String): ConsultationResults? {
        return when (val cacheResult = cacheRepository.getConsultationResults(consultationId = consultationId)) {
            is ConsultationResultsCacheResult.CachedConsultationResults -> cacheResult.results
            ConsultationResultsCacheResult.ConsultationResultsNotFound -> null
            ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized -> buildConsultationResults(
                consultationId = consultationId
            ).also { results ->
                if (results != null) {
                    cacheRepository.initConsultationResults(consultationId = consultationId, results = results)
                } else {
                    cacheRepository.initConsultationResultsNotFound(consultationId = consultationId)
                }
            }
        }
    }

    fun buildConsultationResults(consultationId: String): ConsultationResults? {
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId = consultationId)
            ?: return null

        val questionList = questionRepository.getConsultationQuestionList(consultationId = consultationId)
            .filterIsInstance<QuestionWithChoices>()
        val participantCount = userAnsweredConsultationRepository.getParticipantCount(consultationId = consultationId)
        val consultationResponseList = if (questionList.isNotEmpty()) {
            consultationResultAggregatedRepository.getConsultationResultAggregated(consultationId = consultationId)
                .map { consultationResultAggregated ->
                    ResponseConsultationCount(
                        questionId = consultationResultAggregated.questionId,
                        choiceId = consultationResultAggregated.choiceId,
                        responseCount = consultationResultAggregated.responseCount,
                    )
                }.ifEmpty {
                    consultationResponseRepository.getConsultationResponsesCount(consultationId = consultationId)
                }
        } else emptyList()

        return ConsultationResults(
            consultation = consultationInfo,
            participantCount = participantCount,
            results = questionList
                .filter { question -> question.choixPossibleList.isNotEmpty() }
                .map { question -> mapper.toQuestionNoResponse(question) }
                .map { question ->
                    buildQuestionResults(
                        question = question,
                        participantCount = participantCount,
                        consultationResponseList = consultationResponseList,
                    )
                }
        )
    }

    private fun buildQuestionResults(
        question: QuestionWithChoices,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
    ) = QuestionResults(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildChoiceResults(
                question = question,
                choix = choix,
                participantCount = participantCount,
                consultationResponseList = consultationResponseList,
            )
        }
    )

    private fun buildChoiceResults(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
    ): ChoiceResults {
        val choixCount = consultationResponseList
            .filter { it.questionId == question.id && it.choiceId == choix.id }
            .sumOf { it.responseCount }

        return ChoiceResults(
            choixPossible = choix,
            ratio = (choixCount.toDouble() / participantCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }
}