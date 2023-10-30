package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class QuestionCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val QUESTION_CACHE_NAME = "questionCache"
    }

    sealed class CacheResult {
        data class CachedQuestionList(val questionDTOList: List<QuestionDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getQuestionList(consultationId: UUID): CacheResult {
        val questionDTOList = try {
            getCache()?.get(consultationId.toString(), List::class.java) as? List<QuestionDTO>
        } catch (e: IllegalStateException) {
            null
        }

        return when (questionDTOList) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedQuestionList(questionDTOList)
        }
    }

    fun insertQuestionList(consultationId: UUID, questionDTOList: List<QuestionDTO>) {
        getCache()?.put(consultationId.toString(), questionDTOList)
    }

    private fun getCache() = cacheManager.getCache(QUESTION_CACHE_NAME)

}