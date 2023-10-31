package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.Question
import fr.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.gouv.agora.infrastructure.question.dto.QuestionDTO
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QuestionRepositoryImpl(
    private val questionCacheRepository: QuestionCacheRepository,
    private val questionDatabaseRepository: QuestionDatabaseRepository,
    private val choixPossibleCacheRepository: ChoixPossibleCacheRepository,
    private val choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository,
    private val questionMapper: QuestionMapper,
) : QuestionRepository {

    override fun getConsultationQuestionList(consultationId: String): List<Question> {
        return try {
            val consultationUUID = UUID.fromString(consultationId)

            val questionDTOList = getQuestionDTOList(consultationUUID)
            questionDTOList.map { questionDTO ->
                val choixPossibleDTOList = getChoixPossibleDTOList(questionDTO.id)
                questionMapper.toDomain(
                    dto = questionDTO,
                    questionDTOList = questionDTOList,
                    choixPossibleDTOList = choixPossibleDTOList,
                )
            }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getQuestionDTOList(consultationId: UUID): List<QuestionDTO> {
        return when (val cacheResult = questionCacheRepository.getQuestionList(consultationId)) {
            QuestionCacheRepository.CacheResult.CacheNotInitialized -> getQuestionListFromDatabase(consultationId)
            is QuestionCacheRepository.CacheResult.CachedQuestionList -> cacheResult.questionDTOList
        }
    }

    private fun getQuestionListFromDatabase(consultationId: UUID): List<QuestionDTO> {
        val questionDTOList = questionDatabaseRepository.getQuestionConsultation(consultationId) ?: emptyList()
        questionCacheRepository.insertQuestionList(consultationId, questionDTOList)
        return questionDTOList
    }

    private fun getChoixPossibleDTOList(questionId: UUID): List<ChoixPossibleDTO> {
        return when (val cacheResult = choixPossibleCacheRepository.getChoixPossibleList(questionId)) {
            ChoixPossibleCacheRepository.CacheResult.CacheNotInitialized -> getChoixPossibleListFromDatabase(questionId)
            is ChoixPossibleCacheRepository.CacheResult.CachedChoixPossibleList -> cacheResult.choixPossibleDTOList
        }
    }

    private fun getChoixPossibleListFromDatabase(questionId: UUID): List<ChoixPossibleDTO> {
        val choixPossibleDTOList = choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionId) ?: emptyList()
        choixPossibleCacheRepository.insertChoixPossibleList(questionId, choixPossibleDTOList)
        return choixPossibleDTOList
    }

}