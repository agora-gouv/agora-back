package fr.social.gouv.agora.infrastructure.qagPreview.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.social.gouv.agora.usecase.qag.QagWithSupportCount
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagPreviewPageCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : QagPreviewPageCacheRepository {

    companion object {
        private const val QAG_PREVIEW_PAGE_CACHE_NAME = "qagPreviewPageV2"

        private const val POPULAR_PREFIX_KEY = "popular"
        private const val LATEST_PREFIX_KEY = "latest"
        private const val SUPPORTED_PREFIX_KEY = "supported"
    }

    override fun getQagPopularList(thematiqueId: String?) = getQagList("$POPULAR_PREFIX_KEY/$thematiqueId")

    override fun initQagPopularList(thematiqueId: String?, qagList: List<QagWithSupportCount>) {
        initQagList("$POPULAR_PREFIX_KEY/$thematiqueId", qagList)
    }

    override fun evictQagPopularList(thematiqueId: String?) {
        getCache()?.evict("$POPULAR_PREFIX_KEY/$thematiqueId")
    }

    override fun getQagLatestList(thematiqueId: String?) = getQagList("$LATEST_PREFIX_KEY/$thematiqueId")

    override fun initQagLatestList(thematiqueId: String?, qagList: List<QagWithSupportCount>) {
        initQagList("$LATEST_PREFIX_KEY/$thematiqueId", qagList)
    }

    override fun evictQagLatestList(thematiqueId: String?) {
        getCache()?.evict("$LATEST_PREFIX_KEY/$thematiqueId")
    }

    override fun getQagSupportedList(userId: String, thematiqueId: String?) = getQagList("$SUPPORTED_PREFIX_KEY$userId")

    override fun initQagSupportedList(
        userId: String,
        thematiqueId: String?,
        qagList: List<QagWithSupportCount>,
    ) {
        initQagList("$SUPPORTED_PREFIX_KEY$userId", qagList)
    }

    override fun evictQagSupportedList(userId: String, thematiqueId: String?) {
        getCache()?.evict("$SUPPORTED_PREFIX_KEY$userId")
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(QAG_PREVIEW_PAGE_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getQagList(key: String): List<QagWithSupportCount>? {
        return try {
            val modelList = getCache()?.get(key, List::class.java) as? List<String>
            return modelList?.map { objectMapper.readValue(it, QagWithSupportCount::class.java) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun initQagList(key: String, qagList: List<QagWithSupportCount>) {
        getCache()?.put(key, qagList.map { objectMapper.writeValueAsString(it) })
    }
}