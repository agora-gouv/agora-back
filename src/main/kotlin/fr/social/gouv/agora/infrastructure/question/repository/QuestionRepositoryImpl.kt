package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import fr.social.gouv.agora.infrastructure.question.repository.QuestionRepositoryImpl.Companion.CHOIX_POSSIBLE_CACHE_NAME
import fr.social.gouv.agora.infrastructure.question.repository.QuestionRepositoryImpl.Companion.QUESTION_CACHE_NAME
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
@CacheConfig(cacheNames = [QUESTION_CACHE_NAME, CHOIX_POSSIBLE_CACHE_NAME])
class QuestionRepositoryImpl(
    private val questionDatabaseRepository: QuestionDatabaseRepository,
    private val choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository,
    private val questionMapper: QuestionMapper,
    private val cacheManager: CacheManager,
) : QuestionRepository {

    companion object {
        const val QUESTION_CACHE_NAME = "questionCache"
        const val CHOIX_POSSIBLE_CACHE_NAME = "choixPossibleCache"
    }

    override fun getConsultationQuestionList(consultationId: String): List<Question> {
        return try {
            val uuid = UUID.fromString(consultationId)
            val questionDtoList = getQuestionConsultationFromCache(uuid) ?: getQuestionConsultationFromDatabase(uuid)
            questionDtoList.map { questionDto ->
                val choixPossibleDtoList =
                    getChoixPossibleListFromCache(questionDto.id) ?: getChoixPossibleListFromDatabase(questionDto.id)
                questionMapper.toDomain(questionDto, choixPossibleDtoList)
            }
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    private fun getCacheQuestion() = cacheManager.getCache(QUESTION_CACHE_NAME)
    private fun getCacheChoixPossible() = cacheManager.getCache(CHOIX_POSSIBLE_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getQuestionConsultationFromCache(idConsultation: UUID): List<QuestionDTO>? {
        return getCacheQuestion()?.get(idConsultation.toString(), List::class.java) as? List<QuestionDTO>
    }

    private fun getQuestionConsultationFromDatabase(idConsultation: UUID): List<QuestionDTO> {
        val questionListDto = questionDatabaseRepository.getQuestionConsultation(idConsultation) ?: emptyList()
        getCacheQuestion()?.put(idConsultation.toString(), questionListDto)
        return questionListDto
    }

    @Suppress("UNCHECKED_CAST")
    private fun getChoixPossibleListFromCache(idQuestion: UUID): List<ChoixPossibleDTO>? {
        return getCacheChoixPossible()?.get(idQuestion.toString(), List::class.java) as? List<ChoixPossibleDTO>
    }

    private fun getChoixPossibleListFromDatabase(idQuestion: UUID): List<ChoixPossibleDTO> {
        val choixPossibleListDto = choixPossibleDatabaseRepository.getChoixPossibleQuestion(idQuestion) ?: emptyList()
        getCacheChoixPossible()?.put(idQuestion.toString(), choixPossibleListDto)
        return choixPossibleListDto
    }

}