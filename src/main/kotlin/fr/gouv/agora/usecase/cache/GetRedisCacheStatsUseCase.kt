package fr.gouv.agora.usecase.cache

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Properties

/**
 * Use case d'observabilité du cache Redis.
 *
 * Fournit, sans bloquer Redis :
 *  - le nombre total de clés (`DBSIZE`),
 *  - la consommation mémoire et la politique d'éviction (`INFO memory`),
 *  - le nombre de clés par préfixe de cache (nom de cache Spring, préfixe avant `::`),
 *    obtenu via un unique parcours `SCAN` (jamais `KEYS`, qui bloquerait Redis).
 *
 * Le `SCAN` est non bloquant et incrémental : il ne verrouille pas Redis, même sur un
 * keyspace volumineux. Le comptage par préfixe permet d'identifier les caches qui
 * accumulent des clés (ex. caches à clé par utilisateur).
 */
@Service
class GetRedisCacheStatsUseCase(
    private val redisConnectionFactory: RedisConnectionFactory,
) {
    private val logger: Logger = LoggerFactory.getLogger(GetRedisCacheStatsUseCase::class.java)

    companion object {
        private const val SCAN_COUNT = 1000L
        private const val CACHE_KEY_SEPARATOR = "::"
        private const val UNKNOWN_PREFIX = "<sans-préfixe>"
    }

    fun getStats(): RedisCacheStats {
        return redisConnectionFactory.connection.use { connection ->
            val totalKeys = connection.serverCommands().dbSize() ?: 0L
            val memoryInfo = try {
                connection.serverCommands().info("memory") ?: Properties()
            } catch (e: Exception) {
                logger.warn("Impossible de lire INFO memory depuis Redis : {}", e.message)
                Properties()
            }

            RedisCacheStats(
                totalKeys = totalKeys,
                keysByCachePrefix = countKeysByCachePrefix(connection),
                usedMemoryHuman = memoryInfo.getProperty("used_memory_human"),
                usedMemoryBytes = memoryInfo.getProperty("used_memory")?.toLongOrNull(),
                maxMemoryHuman = memoryInfo.getProperty("maxmemory_human"),
                maxMemoryBytes = memoryInfo.getProperty("maxmemory")?.toLongOrNull(),
                maxMemoryPolicy = memoryInfo.getProperty("maxmemory_policy"),
                memoryFragmentationRatio = memoryInfo.getProperty("mem_fragmentation_ratio"),
            )
        }
    }

    private fun countKeysByCachePrefix(connection: RedisConnection): List<CachePrefixKeyCount> {
        val counts = mutableMapOf<String, Long>()
        try {
            connection.keyCommands()
                .scan(ScanOptions.scanOptions().count(SCAN_COUNT).build())
                .use { cursor ->
                    while (cursor.hasNext()) {
                        val key = String(cursor.next(), StandardCharsets.UTF_8)
                        val prefix = key.substringBefore(CACHE_KEY_SEPARATOR).ifEmpty { UNKNOWN_PREFIX }
                        counts[prefix] = (counts[prefix] ?: 0L) + 1L
                    }
                }
        } catch (e: Exception) {
            logger.warn("Impossible de scanner les clés Redis pour les statistiques : {}", e.message)
        }
        return counts.entries
            .sortedByDescending { it.value }
            .map { CachePrefixKeyCount(prefix = it.key, keyCount = it.value) }
    }
}

data class RedisCacheStats(
    val totalKeys: Long,
    val keysByCachePrefix: List<CachePrefixKeyCount>,
    val usedMemoryHuman: String?,
    val usedMemoryBytes: Long?,
    val maxMemoryHuman: String?,
    val maxMemoryBytes: Long?,
    val maxMemoryPolicy: String?,
    val memoryFragmentationRatio: String?,
)

data class CachePrefixKeyCount(
    val prefix: String,
    val keyCount: Long,
)
