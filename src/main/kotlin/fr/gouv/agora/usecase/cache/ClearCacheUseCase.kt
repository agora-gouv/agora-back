package fr.gouv.agora.usecase.cache

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
) {
    private val logger: Logger = LoggerFactory.getLogger(ClearCacheUseCase::class.java)

    fun clearAllCaches(): Int {
        val allManagers = listOf(cacheManager, shortTermCacheManager, longTermCacheManager, eternalCacheManager)
        var clearedCount = 0

        allManagers.forEach { manager ->
            manager.cacheNames.forEach { cacheName ->
                manager.getCache(cacheName)?.clear()
                logger.info("Cache vidé : $cacheName")
                clearedCount++
            }
        }

        logger.info("Total caches vidés : $clearedCount")
        return clearedCount
    }
}
