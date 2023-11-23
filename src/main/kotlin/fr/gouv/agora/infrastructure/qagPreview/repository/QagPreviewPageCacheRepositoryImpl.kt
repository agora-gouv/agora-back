package fr.gouv.agora.infrastructure.qagPreview.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.usecase.qag.repository.QagPreviewCacheRepository
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagPreviewPageCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : QagPreviewCacheRepository {

    companion object {
        private const val QAG_PREVIEW_PAGE_CACHE_NAME = "qagPreviewPageV2"

        private const val POPULAR_PREFIX_KEY = "popular"
        private const val LATEST_PREFIX_KEY = "latest"
        private const val SUPPORTED_PREFIX_KEY = "supported"
    }

    override fun getQagPopularList(thematiqueId: String?) = getQagList(key = "$POPULAR_PREFIX_KEY/$thematiqueId")

    override fun initQagPopularList(thematiqueId: String?, qagList: List<QagWithSupportCount>) {
        initQagList(key = "$POPULAR_PREFIX_KEY/$thematiqueId", qagList = qagList)
    }

    override fun incrementQagPopularSupportCount(thematiqueId: String?, qagId: String) {
        incrementSupportCount(key = "$POPULAR_PREFIX_KEY/$thematiqueId", qagId = qagId)
    }

    override fun decrementQagPopularSupportCount(thematiqueId: String?, qagId: String) {
        decrementSupportCount(key = "$POPULAR_PREFIX_KEY/$thematiqueId", qagId = qagId)
    }

    override fun evictQagPopularList(thematiqueId: String?) {
        getCache()?.evict("$POPULAR_PREFIX_KEY/$thematiqueId")
    }

    override fun getQagLatestList(thematiqueId: String?) = getQagList("$LATEST_PREFIX_KEY/$thematiqueId")

    override fun initQagLatestList(thematiqueId: String?, qagList: List<QagWithSupportCount>) {
        initQagList("$LATEST_PREFIX_KEY/$thematiqueId", qagList)
    }

    override fun incrementQagLatestSupportCount(thematiqueId: String?, qagId: String) {
        incrementSupportCount(key = "$LATEST_PREFIX_KEY/$thematiqueId", qagId = qagId)
    }

    override fun decrementQagLatestSupportCount(thematiqueId: String?, qagId: String) {
        decrementSupportCount(key = "$LATEST_PREFIX_KEY/$thematiqueId", qagId = qagId)
    }

    override fun evictQagLatestList(thematiqueId: String?) {
        getCache()?.evict("$LATEST_PREFIX_KEY/$thematiqueId")
    }

    override fun getQagSupportedList(userId: String, thematiqueId: String?) = getQagList("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId")

    override fun initQagSupportedList(
        userId: String,
        thematiqueId: String?,
        qagList: List<QagWithSupportCount>,
    ) {
        initQagList("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId", qagList)
    }

    override fun incrementSupportedSupportCount(userId: String, thematiqueId: String?, qagId: String) {
        incrementSupportCount(key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId", qagId = qagId)
    }

    override fun decrementSupportedSupportCount(userId: String, thematiqueId: String?, qagId: String) {
        decrementSupportCount(key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId", qagId = qagId)
    }

    override fun evictQagSupportedList(userId: String, thematiqueId: String?) {
        getCache()?.evict("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId")
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(QAG_PREVIEW_PAGE_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getQagList(key: String): List<QagWithSupportCount>? {
        return try {
            val modelList = getCache()?.get(key, List::class.java) as? List<String>
            modelList?.map { objectMapper.readValue(it, QagWithSupportCount::class.java) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun initQagList(key: String, qagList: List<QagWithSupportCount>) {
        getCache()?.put(key, qagList.map { objectMapper.writeValueAsString(it) })
    }

    private fun incrementSupportCount(key: String, qagId: String) {
        alterQag(key, qagId, alterQagMethod = { qag ->
            qag.copy(qagInfo = qag.qagInfo.copy(supportCount = qag.qagInfo.supportCount + 1))
        })
    }

    private fun decrementSupportCount(key: String, qagId: String) {
        alterQag(key, qagId, alterQagMethod = { qag ->
            qag.copy(qagInfo = qag.qagInfo.copy(supportCount = qag.qagInfo.supportCount - 1))
        })
    }

    private fun alterQag(key: String, qagId: String, alterQagMethod: (QagWithSupportCount) -> QagWithSupportCount) {
        getQagList(key = key)?.let { qagList ->
            if (qagList.any { qag -> qag.qagInfo.id == qagId }) {
                qagList.map { qag ->
                    if (qag.qagInfo.id == qagId) {
                        alterQagMethod.invoke(qag)
                    } else qag
                }
            }
        }
    }
}