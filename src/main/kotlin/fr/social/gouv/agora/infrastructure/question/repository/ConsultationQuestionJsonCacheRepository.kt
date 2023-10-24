package fr.social.gouv.agora.infrastructure.question.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.infrastructure.question.QuestionsJson
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

interface ConsultationQuestionJsonCacheRepository {
    fun getConsultationQuestions(consultationId: String): QuestionsJson?
    fun insertConsultationQuestions(consultationId: String, questions: QuestionsJson)
}

@Component
@Suppress("unused")
class ConsultationQuestionJsonCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationQuestionJsonCacheRepository {

    companion object {
        private const val CONSULTATION_QUESTION_JSON_CACHE_NAME = "consultationQuestions"
    }

    override fun getConsultationQuestions(consultationId: String): QuestionsJson? {
        return getCache()?.get(consultationId, String::class.java)
            ?.let { cacheContent ->
                try {
                    objectMapper.readValue(cacheContent, QuestionsJson::class.java)
                } catch (e: Exception) {
                    null
                }
            }
    }

    override fun insertConsultationQuestions(consultationId: String, questions: QuestionsJson) {
        getCache()?.put(consultationId, objectMapper.writeValueAsString(questions))
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_QUESTION_JSON_CACHE_NAME)

}