package fr.social.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class ConsultationPreviewPageRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationPreviewPageRepository {

    companion object {
        private const val CONSULTATION_PREVIEW_PAGE_CACHE_NAME = "consultationPreviewPage"

        private const val ONGOING_CACHE_PREFIX = "ongoing"
        private const val FINISHED_CACHE_KEY = "finished"
        private const val ANSWERED_CACHE_PREFIX = "answered"
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewOngoingList(userId: String): List<ConsultationPreviewOngoing>? {
        return try {
            val modelList = getCache()?.get("$ONGOING_CACHE_PREFIX/$userId", List::class.java) as? List<String>
            return modelList?.map { objectMapper.readValue(it, ConsultationPreviewOngoing::class.java) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewOngoingList(userId: String, ongoingList: List<ConsultationPreviewOngoing>) {
        getCache()?.put("$ONGOING_CACHE_PREFIX/$userId", ongoingList.map { objectMapper.writeValueAsString(it) })
    }

    override fun evictConsultationPreviewOngoingList(userId: String) {
        getCache()?.evict("$ONGOING_CACHE_PREFIX/$userId")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished>? {
        return try {
            val modelList = getCache()?.get(FINISHED_CACHE_KEY, List::class.java) as? List<String>
            return modelList?.map { objectMapper.readValue(it, ConsultationPreviewFinished::class.java) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewFinishedList(finishedList: List<ConsultationPreviewFinished>) {
        getCache()?.put(FINISHED_CACHE_KEY, finishedList.map { objectMapper.writeValueAsString(it) })
    }

    override fun evictConsultationPreviewFinishedList() {
        getCache()?.evict(FINISHED_CACHE_KEY)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewAnsweredList(userId: String): List<ConsultationPreviewAnswered>? {
        return try {
            val modelList = getCache()?.get(ANSWERED_CACHE_PREFIX, List::class.java) as? List<String>
            return modelList?.map { objectMapper.readValue(it, ConsultationPreviewAnswered::class.java) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewAnsweredList(
        userId: String,
        answeredList: List<ConsultationPreviewAnswered>,
    ) {
        getCache()?.put("$ANSWERED_CACHE_PREFIX$userId", answeredList.map { objectMapper.writeValueAsString(it) })
    }

    override fun evictConsultationPreviewAnsweredList(userId: String) {
        getCache()?.evict("$ANSWERED_CACHE_PREFIX$userId")
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_PREVIEW_PAGE_CACHE_NAME)
}