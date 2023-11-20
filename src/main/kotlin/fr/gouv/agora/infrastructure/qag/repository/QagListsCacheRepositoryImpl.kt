package fr.gouv.agora.infrastructure.qag.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.usecase.qagPaginated.QagsAndMaxPageCount
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
    }

    override fun getQagPopularList(thematiqueId: String?, pageNumber: Int) =
        getQagList(key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber")

    override fun initQagPopularList(thematiqueId: String?, pageNumber: Int, qagsAndMaxPageCount: QagsAndMaxPageCount) {
        initQagList(key = "$TOP_PREFIX_KEY/$thematiqueId/$pageNumber", qagsAndMaxPageCount = qagsAndMaxPageCount)
    }

    override fun evictQagPopularList(thematiqueId: String?, pageNumber: Int) {
        getCache()?.evict("$TOP_PREFIX_KEY/$thematiqueId/$pageNumber")
    }

    override fun getQagLatestList(thematiqueId: String?, pageNumber: Int) =
        getQagList("$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber")

    override fun initQagLatestList(thematiqueId: String?, pageNumber: Int, qagsAndMaxPageCount: QagsAndMaxPageCount) {
        initQagList(key = "$LATEST_PREFIX_KEY/$thematiqueId/$pageNumber", qagsAndMaxPageCount = qagsAndMaxPageCount)
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
        qagsAndMaxPageCount: QagsAndMaxPageCount,
    ) {
        initQagList(
            key = "$SUPPORTED_PREFIX_KEY/$userId/$thematiqueId/$pageNumber",
            qagsAndMaxPageCount = qagsAndMaxPageCount
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
    private fun getQagList(key: String): QagsAndMaxPageCount? {
        return try {
            println("salut du cache")
            val value = getCache()?.get(key, Pair::class.java) as? Pair<Int, List<String>>
            value?.first?.let { maxPageCount ->
                value.second.map { objectMapper.readValue(it, QagPreview::class.java) }.let { qags ->
                    QagsAndMaxPageCount(
                        maxPageCount = maxPageCount,
                        qags = qags
                    )
                }
            }

        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun initQagList(key: String, qagsAndMaxPageCount: QagsAndMaxPageCount) {
        getCache()?.put(
            key,
            qagsAndMaxPageCount.maxPageCount to qagsAndMaxPageCount.qags.map { objectMapper.writeValueAsString(it) })
    }
}