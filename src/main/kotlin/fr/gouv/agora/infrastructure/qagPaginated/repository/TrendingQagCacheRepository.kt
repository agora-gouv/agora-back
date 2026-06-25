package fr.gouv.agora.infrastructure.qagPaginated.repository

import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class TrendingQagCacheRepository(@Qualifier("shortTermCacheManager") private val cacheManager: CacheManager) {

    companion object {
        private const val TRENDING_QAG_CACHE_NAME = "trendingQagCache"
        private const val CACHE_KEY = "trendingQagList"
    }

    sealed class CacheResult {
        data class CachedTrendingQagList(val qags: List<QagInfoWithSupportCount>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getTrendingQagList(): CacheResult {
        val cachedList = try {
            getCache()?.get(CACHE_KEY, List::class.java) as? List<QagInfoWithSupportCount>
        } catch (e: IllegalStateException) {
            null
        }
        return cachedList?.let { CacheResult.CachedTrendingQagList(it) }
            ?: CacheResult.CacheNotInitialized
    }

    fun insertTrendingQagList(qags: List<QagInfoWithSupportCount>) {
        getCache()?.put(CACHE_KEY, qags)
    }

    private fun getCache() = cacheManager.getCache(TRENDING_QAG_CACHE_NAME)
}
