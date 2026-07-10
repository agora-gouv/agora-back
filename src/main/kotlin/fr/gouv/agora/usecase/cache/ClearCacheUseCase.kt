package fr.gouv.agora.usecase.cache

import fr.gouv.agora.usecase.qagPaginated.repository.TrendingClusterRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service

@Service
class ClearCacheUseCase(
    private val cacheManager: CacheManager,
    @Qualifier("shortTermCacheManager") private val shortTermCacheManager: CacheManager,
    @Qualifier("longTermCacheManager") private val longTermCacheManager: CacheManager,
    @Qualifier("eternalCacheManager") private val eternalCacheManager: CacheManager,
    private val trendingClusterRepository: TrendingClusterRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(ClearCacheUseCase::class.java)

    fun clearAllCaches(): List<String> {
        val allManagers = listOf(cacheManager, shortTermCacheManager, longTermCacheManager, eternalCacheManager)
        val clearedCacheNames = mutableListOf<String>()

        allManagers.forEach { manager ->
            manager.cacheNames.forEach { cacheName ->
                manager.getCache(cacheName)?.clear()
                logger.info("Cache vidé : $cacheName")
                clearedCacheNames.add(cacheName)
            }
        }

        trendingClusterRepository.clearCache()
        logger.info("Cache vidé : trendingClusterCache")
        clearedCacheNames.add("trendingClusterCache")

        logger.info("Total caches vidés : ${clearedCacheNames.size}")
        return clearedCacheNames
    }
}
