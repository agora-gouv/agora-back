package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.social.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val getConsultationResponseRepository: GetConsultationResponseRepository,
    private val consultationUpdateUseCase: ConsultationUpdateUseCase,
    private val mapper: QuestionNoResponseMapper,
) {

    fun getConsultationResults(consultationId: String): ConsultationResult? {
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId) ?: return null
        val consultationUpdate = consultationUpdateUseCase.getConsultationUpdate(consultationInfo) ?: return null
        val questionList =
            questionRepository.getConsultationQuestionList(consultationId).takeUnless { it.isEmpty() } ?: return null
        val consultationResponseList = getConsultationResponseRepository.getConsultationResponses(consultationId)

        return buildResults(
            consultationInfo = consultationInfo,
            consultationUpdate = consultationUpdate,
            questionList = questionList,
            consultationResponseList = consultationResponseList
        )
    }

    private fun buildResults(
        consultationInfo: ConsultationInfo,
        consultationUpdate: ConsultationUpdate,
        questionList: List<Question>,
        consultationResponseList: List<ReponseConsultation>,
    ): ConsultationResult {
        val filteredQuestionList = questionList.filterIsInstance<QuestionWithChoices>()
            .filter { it.choixPossibleList.isNotEmpty() }
            .map { questionWithChoices -> mapper.toQuestionNoResponse(questionWithChoices) }
        val participantCount = consultationResponseList.map { it.participationId }.toSet().size

        return ConsultationResult(
            consultation = consultationInfo,
            lastUpdate = consultationUpdate,
            participantCount = participantCount,
            results = filteredQuestionList.map { question ->
                buildQuestionResults(
                    question = question,
                    participantCount = participantCount,
                    consultationResponseList = consultationResponseList
                )
            }
        )
    }

    private fun buildQuestionResults(
        question: QuestionWithChoices,
        participantCount: Int,
        consultationResponseList: List<ReponseConsultation>,
    ) = QuestionResult(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildQuestionResult(
                question = question,
                choix = choix,
                participantCount = participantCount,
                consultationResponseList = consultationResponseList,
            )
        }
    )

    private fun buildQuestionResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ReponseConsultation>,
    ): ChoiceResult {
        val choixCount = consultationResponseList.filter {
            it.questionId == question.id && it.choiceId == choix.id
        }.size

        return ChoiceResult(
            choixPossible = choix,
            ratio = (choixCount.toDouble() / participantCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }
}