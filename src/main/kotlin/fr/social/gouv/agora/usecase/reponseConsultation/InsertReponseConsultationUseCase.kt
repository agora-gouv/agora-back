package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.Question
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
        private const val OTHER_QUESTION_MAX_LENGTH = 200
        private const val OPEN_QUESTION_MAX_LENGTH = 400
    }

    fun insertReponseConsultation(
        consultationId: String,
        userId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): InsertResult {
        if (consultationResponseRepository.hasAnsweredConsultation(consultationId = consultationId, userId = userId)) {
            println("⚠️ Insert response consultation error: user has already answered this consultation")
            return InsertResult.INSERT_FAILURE
        }
        val questionList = questionRepository.getConsultationQuestionList(consultationId)

        val filledConsultationResponses = addMissingResponseIfQuestionWithChoices(
            consultationResponses = consultationResponses,
            questionList = questionList,
        )
        consultationPreviewAnsweredRepository.deleteConsultationAnsweredListFromCache(userId)
        consultationPreviewPageRepository.evictConsultationPreviewOngoingList(userId)
        consultationPreviewPageRepository.evictConsultationPreviewAnsweredList(userId)

        val responseInsertResult = insertReponseConsultationRepository.insertConsultationResponses(
            insertParameters = insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = consultationId,
                userId = userId,
            ),
            consultationResponses = sanitizeConsultationResponse(filledConsultationResponses, questionList),
        )
        if (responseInsertResult == InsertResult.INSERT_FAILURE)
            println("⚠️ Insert response consultation error")
        return responseInsertResult
    }

    private fun addMissingResponseIfQuestionWithChoices(
        consultationResponses: List<ReponseConsultationInserting>,
        questionList: List<Question>,
    ) = questionList.mapNotNull { question ->
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
        questionList: List<Question>,
    ): List<ReponseConsultationInserting> {
        return consultationResponses.map { response ->
            val lengthSanitizedContent = when (questionList.find { it.id == response.questionId }) {
                is QuestionOpen -> OPEN_QUESTION_MAX_LENGTH
                else -> OTHER_QUESTION_MAX_LENGTH
            }
            if (response.responseText.isNotEmpty()) {
                ReponseConsultationInserting(
                    questionId = response.questionId,
                    choiceIds = response.choiceIds,
                    responseText = contentSanitizer.sanitize(response.responseText, lengthSanitizedContent)
                )
            } else {
                response.copy()
            }
        }
    }
}
