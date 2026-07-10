package fr.gouv.agora.infrastructure.qagPaginated.repository

import fr.gouv.agora.domain.TrendingCluster
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class TrendingClusterCacheRepository(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
) {
    companion object {
        private const val CACHE_NAME = "trendingClusterCache"
        private const val CACHE_KEY = "trendingClusterList"
    }

    sealed class CacheResult {
        data class CachedClusters(val clusters: List<TrendingCluster>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getClusters(): CacheResult {
        val cached = try {
            getCache()?.get(CACHE_KEY, List::class.java) as? List<TrendingCluster>
        } catch (e: IllegalStateException) {
            null
        }
        return cached?.let { CacheResult.CachedClusters(it) } ?: CacheResult.CacheNotInitialized
    }

    fun insertClusters(clusters: List<TrendingCluster>) {
        getCache()?.put(CACHE_KEY, clusters)
    }

    fun clearCache() {
        getCache()?.clear()
    }

    private fun getCache() = cacheManager.getCache(CACHE_NAME)
}
