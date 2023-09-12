package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.QuestionWithChoices
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.stereotype.Service

@Service
class InsertReponseConsultationUseCase(
    private val consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository,
    private val insertReponseConsultationRepository: InsertReponseConsultationRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val questionRepository: QuestionRepository,
    private val insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper,
) {

    fun insertReponseConsultation(
        consultationId: String,
        userId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): InsertResult {
        if (consultationResponseRepository.hasAnsweredConsultation(consultationId = consultationId, userId = userId)) {
            return InsertResult.INSERT_FAILURE
        }

        val filledConsultationResponses = addMissingResponseIfQuestionWithChoices(
            consultationId = consultationId,
            consultationResponses = consultationResponses,
        )

        consultationPreviewAnsweredRepository.deleteConsultationAnsweredListFromCache(userId)
        return insertReponseConsultationRepository.insertConsultationResponses(
            insertParameters = insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = consultationId,
                userId = userId,
            ),
            consultationResponses = filledConsultationResponses,
        )
    }

    private fun addMissingResponseIfQuestionWithChoices(
        consultationId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ) = questionRepository.getConsultationQuestionList(consultationId).mapNotNull { question ->
        consultationResponses.find { consultationResponse -> consultationResponse.questionId == question.id }
            ?: question.takeIf { it is QuestionWithChoices }?.let { createNotApplicableResponse(question.id) }
    }

    private fun createNotApplicableResponse(questionId: String) = ReponseConsultationInserting(
        questionId = questionId,
        choiceIds = listOf(UuidUtils.NOT_APPLICABLE_CHOICE_UUID),
        responseText = "",
    )
}
