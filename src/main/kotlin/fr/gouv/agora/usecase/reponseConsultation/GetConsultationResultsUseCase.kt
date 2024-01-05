package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val consultationUpdateUseCase: ConsultationUpdateUseCase,
    private val mapper: QuestionNoResponseMapper,
) {

    fun getConsultationResults(consultationId: String): ConsultationResult? {
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId = consultationId)
            ?: return null
        val consultationUpdate = consultationUpdateUseCase.getConsultationUpdate(consultationInfo = consultationInfo)
            ?: return null

        val questionList = questionRepository.getConsultationQuestionList(consultationId = consultationId)
            .filterIsInstance<QuestionWithChoices>()
        val participantCount = userAnsweredConsultationRepository.getParticipantCount(consultationId = consultationId)
        val consultationResponseList = if (questionList.isNotEmpty()) {
            consultationResponseRepository.getConsultationResponsesCount(consultationId = consultationId)
            // TODO: if aggregated, use dedicated repository instead
        } else emptyList()

        return ConsultationResult(
            consultation = consultationInfo,
            lastUpdate = consultationUpdate,
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
    ) = QuestionResult(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildChoiceResult(
                question = question,
                choix = choix,
                participantCount = participantCount,
                consultationResponseList = consultationResponseList,
            )
        }
    )

    private fun buildChoiceResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
    ): ChoiceResult {
        val choixCount = consultationResponseList
            .filter { it.questionId == question.id && it.choiceId == choix.id }
            .sumOf { it.responseCount }

        return ChoiceResult(
            choixPossible = choix,
            ratio = (choixCount.toDouble() / participantCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }
}