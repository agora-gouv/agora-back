package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.QuestionOpen
import fr.social.gouv.agora.domain.QuestionWithChoices
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.social.gouv.agora.usecase.qag.ContentSanitizer
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.stereotype.Service

@Service
class InsertReponseConsultationUseCase(
    private val contentSanitizer: ContentSanitizer,
    private val consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository,
    private val insertReponseConsultationRepository: InsertReponseConsultationRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val questionRepository: QuestionRepository,
    private val insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper,
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
) {
    companion object {
        private const val AUTRE_QUESTION_MAX_LENGTH = 200
        private const val OPEN_QUESTION_MAX_LENGTH = 400
    }

    fun insertReponseConsultation(
        consultationId: String,
        userId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): InsertResult {
        if (consultationResponseRepository.hasAnsweredConsultation(consultationId = consultationId, userId = userId)) {
            println("⚠️ User already answer the consultation")
            return InsertResult.INSERT_FAILURE
        }

        val filledConsultationResponses = addMissingResponseIfQuestionWithChoices(
            consultationId = consultationId,
            consultationResponses = consultationResponses,
        )

        consultationPreviewAnsweredRepository.deleteConsultationAnsweredListFromCache(userId)
        consultationPreviewPageRepository.evictConsultationPreviewOngoingList(userId)
        consultationPreviewPageRepository.evictConsultationPreviewAnsweredList(userId)
        val responseInsertResult = insertReponseConsultationRepository.insertConsultationResponses(
            insertParameters = insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = consultationId,
                userId = userId,
            ),
            consultationResponses = sanitizeConsultationResponse(filledConsultationResponses, consultationId),
        )
        if (responseInsertResult == InsertResult.INSERT_FAILURE)
            println("⚠️ Insert Response consultation error")
        return responseInsertResult
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

    private fun sanitizeConsultationResponse(
        consultationResponses: List<ReponseConsultationInserting>,
        consultationId: String,
    ): List<ReponseConsultationInserting> {
        val sanitizedResponsesList = mutableListOf<ReponseConsultationInserting>()
        for (reponse in consultationResponses) {
            val question = questionRepository.getConsultationQuestionList(consultationId)
                .find { question -> reponse.questionId == question.id }
            val lenghtSanitizedContent =
                when (question) {
                    is QuestionOpen -> OPEN_QUESTION_MAX_LENGTH
                    else -> AUTRE_QUESTION_MAX_LENGTH
                }
            sanitizedResponsesList.add(
                ReponseConsultationInserting(
                    questionId = reponse.questionId,
                    choiceIds = reponse.choiceIds,
                    responseText = contentSanitizer.sanitize(reponse.responseText, lenghtSanitizedContent)
                )
            )
        }
        return sanitizedResponsesList
    }
}
