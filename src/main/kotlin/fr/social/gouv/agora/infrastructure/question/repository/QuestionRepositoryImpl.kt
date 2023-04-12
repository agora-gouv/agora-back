package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.infrastructure.choixpossible.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.choixpossible.repository.ChoixPossibleDatabaseRepository
import fr.social.gouv.agora.infrastructure.choixpossible.repository.ChoixPossibleMapper
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import fr.social.gouv.agora.infrastructure.question.repository.QuestionRepositoryImpl.Companion.QUESTION_CACHE_NAME
import fr.social.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@CacheConfig(cacheNames = [QUESTION_CACHE_NAME])
class QuestionRepositoryImpl(
    private val questionDatabaseRepository: QuestionDatabaseRepository,
    private val choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository,
    private val questionMapper: QuestionMapper,
    private val choixPossibleMapper: ChoixPossibleMapper,
    private val cacheManager: CacheManager,
) : QuestionRepository {

    companion object {
        const val QUESTION_CACHE_NAME = "questionCache"
        const val CHOIX_POSSIBLE_CACHE_NAME = "choixPossibleCache"
    }

    override fun getQuestionConsultation(id_consultation: String): List<Question>?{
            try {
                val uuid = UUID.fromString(id_consultation)
                var questionList= mutableListOf<Question>()
                var questionDtoList = getQuestionConsultationFromCache(uuid) ?: getQuestionConsultationFromDatabase(uuid)
                if(questionDtoList!= emptyList<QuestionDTO>())
                    {
                    for (questionDto in questionDtoList) {
                        var listeChoixdto = getChoixPossibleQuestionFromCache(questionDto.id) ?: getChoixPossibleQuestionFromDatabase(questionDto.id)
                        var question = questionMapper.toDomain(questionDto, listeChoixdto)
                        questionList.add(question)
                        }
                        return questionList
                    }
                else {return emptyList<Question>()}
            }
            catch (e: IllegalArgumentException){return null}
    }

    private fun getCacheQuestion() = cacheManager.getCache(QUESTION_CACHE_NAME)
    private fun getCacheChoixPossible() = cacheManager.getCache(CHOIX_POSSIBLE_CACHE_NAME)

    private fun getQuestionConsultationFromCache(id_consultation: UUID): List<QuestionDTO>? {
        val CACHE_KEY=id_consultation.toString()
        return getCacheQuestion()?.get(CACHE_KEY, List::class.java) as? List<QuestionDTO>
    }

    private fun getQuestionConsultationFromDatabase(id_consultation: UUID): List<QuestionDTO> {
        val questionListDto = questionDatabaseRepository.getQuestionConsultation(id_consultation)?: emptyList<QuestionDTO>()
        val CACHE_KEY=id_consultation.toString()
        getCacheQuestion()?.put(CACHE_KEY, questionListDto)
        return questionListDto
    }
    private fun getChoixPossibleQuestionFromCache(id_question: UUID): List<ChoixPossibleDTO>? {
        val CACHE_KEY=id_question.toString()
        return getCacheChoixPossible()?.get(CACHE_KEY, List::class.java) as? List<ChoixPossibleDTO>
    }

    private fun getChoixPossibleQuestionFromDatabase(id_question: UUID): List<ChoixPossibleDTO> {
        val choixPossibleListDto = choixPossibleDatabaseRepository.getChoixPossibleQuestion(id_question)?: emptyList<ChoixPossibleDTO>()
        val CACHE_KEY=id_question.toString()
        getCacheChoixPossible()?.put(CACHE_KEY, choixPossibleListDto)
        return choixPossibleListDto
    }


}