package fr.gouv.agora.infrastructure.qag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
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

    override fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber")
    }

    override fun clear() {
        getCache()?.clear()
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
}

data class QagListWithMaxPageCount(
    val maxPageCount: Int,
    val qags: List<QagWithSupportCount>,
)