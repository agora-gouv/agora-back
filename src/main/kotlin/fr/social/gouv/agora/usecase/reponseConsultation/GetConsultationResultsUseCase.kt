package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationRepository
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetReponseConsultationRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsUseCase(
    private val consultationRepository: ConsultationRepository,
    private val questionRepository: QuestionRepository,
    private val getReponseConsultationRepository: GetReponseConsultationRepository,
    private val consultationUpdateRepository: ConsultationUpdateRepository,
) {

    fun getConsultationResults(consultationId: String): ConsultationResult? {
        consultationRepository.getConsultation(consultationId)?.let { consultation ->
            consultationUpdateRepository.getConsultationUpdate(consultationId)?.let { consultationUpdate ->
                questionRepository.getConsultationQuestionList(consultationId).takeUnless { it.isEmpty() }
                    ?.let { questionList ->
                        val consultationResponseList =
                            getReponseConsultationRepository.getConsultationResponses(consultationId)

                        return buildResults(
                            consultation = consultation,
                            consultationUpdate = consultationUpdate,
                            questionList = questionList,
                            consultationResponseList = consultationResponseList
                        )
                    }
            }
        }

        return null
    }

    private fun buildResults(
        consultation: Consultation,
        consultationUpdate: ConsultationUpdate,
        questionList: List<Question>,
        consultationResponseList: List<ReponseConsultation>
    ): ConsultationResult {
        val filteredQuestionList = questionList.filter { it.choixPossibleList.isNotEmpty() }

        return ConsultationResult(
            consultation = consultation,
            lastUpdate = consultationUpdate,
            participantCount = buildParticipantCount(filteredQuestionList, consultationResponseList),
            results = filteredQuestionList.map { question ->
                val questionResponsesCount = consultationResponseList.filter { it.questionId == question.id }.size
                buildQuestionResults(
                    question = question,
                    consultationResponseList = consultationResponseList,
                    questionResponsesCount = questionResponsesCount
                )
            }
        )
    }

    private fun buildQuestionResults(
        question: Question,
        consultationResponseList: List<ReponseConsultation>,
        questionResponsesCount: Int
    ) = QuestionResults(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildQuestionResult(
                consultationResponseList = consultationResponseList,
                question = question,
                choix = choix,
                questionResponsesCount = questionResponsesCount
            )
        }
    )

    private fun buildParticipantCount(
        filteredQuestionList: List<Question>,
        consultationResponseList: List<ReponseConsultation>
    ): Int {
        val participantCount = consultationResponseList.size.toDouble() / filteredQuestionList.size.toDouble()
        return if (participantCount.isNaN()) 0 else participantCount.toInt()
    }

    private fun buildQuestionResult(
        consultationResponseList: List<ReponseConsultation>,
        question: Question,
        choix: ChoixPossible,
        questionResponsesCount: Int
    ): QuestionResult {
        val choixCount =
            consultationResponseList.filter { it.questionId == question.id && it.choiceId == choix.id }.size
        val ratio = choixCount.toDouble() / questionResponsesCount.toDouble()

        return QuestionResult(
            choixPossible = choix,
            ratio = if (ratio.isNaN()) 0.0 else ratio,
        )
    }

}