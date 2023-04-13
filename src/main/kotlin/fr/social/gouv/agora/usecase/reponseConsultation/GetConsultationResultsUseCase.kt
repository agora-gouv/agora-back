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
        val consultation = consultationRepository.getConsultation(consultationId) ?: return null
        val consultationUpdate = consultationUpdateRepository.getConsultationUpdate(consultationId) ?: return null
        val questionList =
            questionRepository.getConsultationQuestionList(consultationId).takeUnless { it.isEmpty() } ?: return null
        val consultationResponseList = getReponseConsultationRepository.getConsultationResponses(consultationId)

        return buildResults(
            consultation = consultation,
            consultationUpdate = consultationUpdate,
            questionList = questionList,
            consultationResponseList = consultationResponseList
        )
    }

    private fun buildResults(
        consultation: Consultation,
        consultationUpdate: ConsultationUpdate,
        questionList: List<Question>,
        consultationResponseList: List<ReponseConsultation>
    ): ConsultationResult {
        val filteredQuestionList = questionList.filter { it.choixPossibleList.isNotEmpty() }
        val participantCount = consultationResponseList.map { it.participationId }.toSet().size

        return ConsultationResult(
            consultation = consultation,
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
        question: Question,
        participantCount: Int,
        consultationResponseList: List<ReponseConsultation>
    ) = QuestionResults(
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
    ): QuestionResult {
        val choixCount = consultationResponseList.filter {
            it.questionId == question.id && it.choiceId == choix.id
        }.size

        return QuestionResult(
            choixPossible = choix,
            ratio = (choixCount.toDouble() / participantCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }

}