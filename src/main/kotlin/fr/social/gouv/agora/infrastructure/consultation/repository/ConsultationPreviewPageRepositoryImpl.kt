package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewAnswered
import fr.social.gouv.agora.domain.ConsultationPreviewFinished
import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationPreviewPageRepositoryImpl(
    private val cacheManager: CacheManager,
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
            getCache()?.get("$ONGOING_CACHE_PREFIX$userId", List::class.java) as? List<ConsultationPreviewOngoing>
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewOngoingList(userId: String, ongoingList: List<ConsultationPreviewOngoing>) {
        getCache()?.put("$ONGOING_CACHE_PREFIX$userId", ongoingList)
    }

    override fun evictConsultationPreviewOngoingList(userId: String) {
        getCache()?.evict("$ONGOING_CACHE_PREFIX$userId")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished>? {
        return try {
            getCache()?.get(FINISHED_CACHE_KEY, List::class.java) as? List<ConsultationPreviewFinished>
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewFinishedList(finishedList: List<ConsultationPreviewFinished>) {
        getCache()?.put(FINISHED_CACHE_KEY, finishedList)
    }

    override fun evictConsultationPreviewFinishedList() {
        getCache()?.evict(FINISHED_CACHE_KEY)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewAnsweredList(userId: String): List<ConsultationPreviewAnswered>? {
        return try {
            getCache()?.get("$ANSWERED_CACHE_PREFIX$userId", List::class.java) as? List<ConsultationPreviewAnswered>
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewAnsweredList(
        userId: String,
        answeredList: List<ConsultationPreviewAnswered>,
    ) {
        getCache()?.put("$ANSWERED_CACHE_PREFIX$userId", answeredList)
    }

    override fun evictConsultationPreviewAnsweredList(userId: String) {
        getCache()?.evict("$ANSWERED_CACHE_PREFIX$userId")
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_PREVIEW_PAGE_CACHE_NAME)

}