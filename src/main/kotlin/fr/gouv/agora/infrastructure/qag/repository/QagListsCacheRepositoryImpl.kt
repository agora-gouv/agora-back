package fr.gouv.agora.infrastructure.qag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.QagWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagListsCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : QagListsCacheRepository {

    companion object {
        private const val QAG_LISTS_PAGINATED_CACHE_NAME = "qagListsPaginatedV2"

        private const val TOP_PREFIX_KEY = "top"
        private const val LATEST_PREFIX_KEY = "latest"
        private const val SUPPORTED_PREFIX_KEY = "supported"
        private const val TRENDING_PREFIX_KEY = "trending"
    }

    override fun getQagPopularList(thematiqueId: String?, pageNumber: Int) =
        getQagList(key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber")

    override fun initQagPopularList(
        thematiqueId: String?,
        pageNumber: Int,
        qagListWithMaxPageCount: QagListWithMaxPageCount,
    ) {
        initQagList(
            key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber",
            qagListWithMaxPageCount = qagListWithMaxPageCount
        )
    }

    override fun incrementQagPopularSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String) {
        incrementSupportCount(key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber", qagId = qagId)
    }

    override fun decrementQagPopularSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String) {
        decrementSupportCount(key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber", qagId = qagId)
    }

    override fun evictQagPopularList(thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$TOP_PREFIX_KEY/$thematiqueId/$pageNumber")
    }

    override fun getQagLatestList(thematiqueId: String?, pageNumber: Int) =
        getQagList("$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber")

    override fun initQagLatestList(
        thematiqueId: String?,
        pageNumber: Int,
        qagListWithMaxPageCount: QagListWithMaxPageCount,
    ) {
        initQagList(
            key = "$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber",
            qagListWithMaxPageCount = qagListWithMaxPageCount
        )
    }

    override fun incrementQagLatestSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String) {
        incrementSupportCount(key = "$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber", qagId = qagId)
    }

    override fun decrementQagLatestSupportCount(thematiqueId: String?, pageNumber: Int, qagId: String) {
        decrementSupportCount(key = "$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber", qagId = qagId)
    }

    override fun evictQagLatestList(thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber")
    }

    override fun getQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int) =
        getQagList("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber")

    override fun initQagSupportedList(
        userId: String,
        thematiqueId: String?,
        pageNumber: Int,
        qagListWithMaxPageCount: QagListWithMaxPageCount,
    ) {
        initQagList(
            key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber",
            qagListWithMaxPageCount = qagListWithMaxPageCount
        )
    }

    override fun incrementSupportedSupportCount(userId: String, thematiqueId: String?, pageNumber: Int, qagId: String) {
        incrementSupportCount(
            key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber",
            qagId = qagId
        )
    }

    override fun decrementSupportedSupportCount(userId: String, thematiqueId: String?, pageNumber: Int, qagId: String) {
        decrementSupportCount(
            key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber",
            qagId = qagId
        )
    }

    override fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber")
    }

    override fun clear() {
        getCache()?.clear()
    }

    override fun getQagTrendingList() = getQagList(key = TRENDING_PREFIX_KEY)

    override fun initQagTrendingList(qagListWithMaxPageCount: QagListWithMaxPageCount) {
        initQagList(
            key = TRENDING_PREFIX_KEY,
            qagListWithMaxPageCount = qagListWithMaxPageCount
        )
    }

    override fun incrementTrendingSupportCount(qagId: String) {
        incrementSupportCount(key = TRENDING_PREFIX_KEY, qagId = qagId)
    }

    override fun decrementTrendingSupportCount(qagId: String) {
        decrementSupportCount(key = TRENDING_PREFIX_KEY, qagId = qagId)
    }

    override fun evictQagTrendingList() {
        getCache()?.evict(TRENDING_PREFIX_KEY)
    }

    private fun getCache() = cacheManager.getCache(QAG_LISTS_PAGINATED_CACHE_NAME)

    private fun getQagList(key: String): QagListWithMaxPageCount? {
        return try {
            getCache()?.get(key, QagListWithMaxPageCount::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun initQagList(key: String, qagListWithMaxPageCount: QagListWithMaxPageCount) {
        getCache()?.put(
            key,
            objectMapper.writeValueAsString(qagListWithMaxPageCount)
        )
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
        getQagList(key = key)?.let { qagListWithMaxPageCount ->
            if (qagListWithMaxPageCount.qags.any { qag -> qag.qagInfo.id == qagId }) {
                qagListWithMaxPageCount.qags.map { qag ->
                    if (qag.qagInfo.id == qagId) {
                        alterQagMethod.invoke(qag)
                    } else qag
                }
            }
        }
    }
}

data class QagListWithMaxPageCount(
    val maxPageCount: Int,
    val qags: List<QagWithSupportCount>,
)