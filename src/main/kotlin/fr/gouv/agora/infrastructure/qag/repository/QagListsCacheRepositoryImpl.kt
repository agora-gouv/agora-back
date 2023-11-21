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
        maxPageCount: Int,
        qags: List<QagWithSupportCount>,
    ) {
        initQagList(key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber", maxPageCount = maxPageCount, qags = qags)
    }

    override fun evictQagPopularList(thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$TOP_PREFIX_KEY/$thematiqueId/$pageNumber")
    }

    override fun getQagLatestList(thematiqueId: String?, pageNumber: Int) =
        getQagList("$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber")

    override fun initQagLatestList(
        thematiqueId: String?,
        pageNumber: Int,
        maxPageCount: Int,
        qags: List<QagWithSupportCount>,
    ) {
        initQagList(key = "$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber", maxPageCount = maxPageCount, qags = qags)
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
        maxPageCount: Int,
        qags: List<QagWithSupportCount>,
    ) {
        initQagList(
            key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber",
            maxPageCount = maxPageCount,
            qags = qags
        )
    }

    override fun evictQagSupportedList(userId: String, thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber")
    }

    override fun clear() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(QAG_LISTS_PAGINATED_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getQagList(key: String): Pair<Int, List<QagWithSupportCount>>? {
        return try {
            val value = getCache()?.get(key, Pair::class.java) as? Pair<Int, List<String>>
            value?.let {
                val count = it.first
                val qags = it.second.mapNotNull { serializedQag ->
                    objectMapper.readValue(
                        serializedQag,
                        QagWithSupportCount::class.java
                    )
                }
                Pair(count, qags)
            }
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun initQagList(key: String, maxPageCount: Int, qags: List<QagWithSupportCount>) {
        getCache()?.put(
            key,
            maxPageCount to qags.map { objectMapper.writeValueAsString(it) })
    }
}