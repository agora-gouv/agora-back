package fr.gouv.agora.infrastructure.responseQag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewList
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ResponseQagPreviewCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ResponseQagPreviewCacheRepository {

    companion object {
        private const val RESPONSE_QAG_PREVIEW_CACHE = "responseQagPreview"
        private const val RESPONSE_QAG_PREVIEW_KEY = "responseList"
    }

    override fun getResponseQagPreviewList(): ResponseQagPreviewList? {
        return try {
            getCache()?.get(RESPONSE_QAG_PREVIEW_KEY, String::class.java)
                ?.let { objectMapper.readValue(it, ResponseQagPreviewList::class.java) }
        } catch (e: Exception) {
            null
        }
    }

    override fun initResponseQagPreviewList(responseList: ResponseQagPreviewList) {
        getCache()?.put(RESPONSE_QAG_PREVIEW_KEY, objectMapper.writeValueAsString(responseList))
    }

    override fun evictResponseQagPreviewList() {
        getCache()?.evict(RESPONSE_QAG_PREVIEW_KEY)
    }

    private fun getCache() = cacheManager.getCache(RESPONSE_QAG_PREVIEW_CACHE)
}
