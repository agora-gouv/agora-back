package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionWithChoices
import fr.gouv.agora.domain.ReponseConsultationInserting
import fr.gouv.agora.domain.UserAnsweredConsultation
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationAnsweredPaginatedListCacheRepository
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheRepository
import fr.gouv.agora.usecase.qag.ContentSanitizer
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class InsertReponseConsultationUseCase(
    private val clock: Clock,
    private val contentSanitizer: ContentSanitizer,
    private val insertReponseConsultationRepository: InsertReponseConsultationRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val questionRepository: QuestionRepository,
    private val insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper,
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val consultationDetailsV2CacheRepository: ConsultationDetailsV2CacheRepository,
    private val consultationAnsweredPaginatedListCacheRepository: ConsultationAnsweredPaginatedListCacheRepository,
    private val consultationResultsCacheRepository: ConsultationResultsCacheRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(InsertReponseConsultationUseCase::class.java)

    companion object {
        private const val OTHER_QUESTION_MAX_LENGTH = 200
        private const val OPEN_QUESTION_MAX_LENGTH = 400
    }

    fun insertReponseConsultation(
        consultationId: String,
        userId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): InsertResult {
        if (consultationInfoRepository.getConsultation(consultationId = consultationId)?.endDate?.isBefore(
                LocalDateTime.now(clock)
            ) == true
        ) {
            logger.error("⚠️ Insert response consultation error: this consultation is already finished")
            return InsertResult.INSERT_FAILURE
        }

        if (userAnsweredConsultationRepository
                .hasAnsweredConsultation(consultationId = consultationId, userId = userId)
        ) {
            logger.error("⚠️ Insert response consultation error: user has already answered this consultation")
            return InsertResult.INSERT_FAILURE
        }

        val questionList = questionRepository.getConsultationQuestions(consultationId).questions

        val filledConsultationResponses = addMissingResponseIfQuestionWithChoices(
            consultationResponses = consultationResponses,
            questionList = questionList,
        )
        consultationPreviewPageRepository.evictConsultationPreviewAnsweredList(userId)
        consultationAnsweredPaginatedListCacheRepository.clearCache(userId)
        consultationDetailsV2CacheRepository.evictHasAnsweredConsultation(
            consultationId = consultationId,
            userId = userId,
        )
        consultationResultsCacheRepository.evictConsultationResultsCache(consultationId = consultationId)

        val responseInsertResult = insertReponseConsultationRepository.insertConsultationResponses(
            insertParameters = insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = consultationId,
                userId = userId,
            ),
            consultationResponses = sanitizeConsultationResponse(filledConsultationResponses, questionList),
        )
        if (responseInsertResult == InsertResult.INSERT_FAILURE)
            logger.error("⚠️ Insert response consultation error")
        else userAnsweredConsultationRepository.insertUserAnsweredConsultation(
            UserAnsweredConsultation(
                userId = userId,
                consultationId = consultationId
            )
        )
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
                response.copy(responseText = contentSanitizer.sanitize(response.responseText, lengthSanitizedContent))
            } else {
                response
            }
        }
    }
}
