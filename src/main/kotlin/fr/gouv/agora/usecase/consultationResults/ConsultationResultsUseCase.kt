package fr.gouv.agora.usecase.consultationResults

import fr.gouv.agora.domain.ChoiceResults
import fr.gouv.agora.domain.ChoixPossible
import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionResults
import fr.gouv.agora.domain.QuestionWithChoices
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.infrastructure.utils.UuidUtils
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

        val questionList = questionRepository.getConsultationQuestions(consultationId = consultationId).questions
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
            resultsWithChoices = questionList.filterIsInstance<QuestionWithChoices>()
                .filter { question -> question.choixPossibleList.isNotEmpty() }
                .map { question -> mapper.toQuestionNoResponse(question) }
                .map { question ->
                    buildQuestionResults(
                        question = question,
                        participantCount = participantCount,
                        consultationResponseList = consultationResponseList,
                    )
                },
            openQuestions = questionList.filterIsInstance<QuestionOpen>(),
        )
    }

    private fun buildQuestionResults(
        question: QuestionWithChoices,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
    ): QuestionResults {
        val notApplicableCount = if (participantCount > 0) {
            consultationResponseList
                .filter { it.questionId == question.id && it.choiceId == UuidUtils.NOT_APPLICABLE_CHOICE_UUID }
                .sumOf { it.responseCount }
        } else 0
        val participantCountWithoutNotApplicable = participantCount - notApplicableCount

        // TODO 20 mai 2025 : désactiver les seenRatio jusqu'au fix des formulaires front qui n'envoient
        // pas les questions vues mais pas répondues
        // ancien code :
        // if (participantCount > 0) {
        //     1 - (notApplicableCount.toDouble() / participantCount)
        // } else 0.0,
        val disableSeenRatio = 1.0

        return QuestionResults(
            question = question,
            seenRatio = disableSeenRatio,
            responses = question.choixPossibleList.map { choix ->
                buildChoiceResults(
                    choix = choix,
                    participantCount = participantCountWithoutNotApplicable,
                    choiceCount = consultationResponseList
                        .filter { it.questionId == question.id && it.choiceId == choix.id }
                        .sumOf { it.responseCount },
                )
            }
        )
    }

    private fun buildChoiceResults(
        choix: ChoixPossible,
        participantCount: Int,
        choiceCount: Int,
    ): ChoiceResults {
        return ChoiceResults(
            choixPossible = choix,
            ratio = (choiceCount.toDouble() / participantCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }
}
