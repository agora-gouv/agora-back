package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.usecase.cache.RedisCacheStats
import io.swagger.v3.oas.annotations.media.Schema

data class RedisCacheStatsJson(
    @Schema(description = "Nombre total de clés présentes dans Redis (DBSIZE)", example = "12345")
    val totalKeys: Long,

    @Schema(description = "Mémoire utilisée par Redis (format lisible)", example = "42.5M")
    val usedMemoryHuman: String?,

    @Schema(description = "Mémoire utilisée par Redis en octets", example = "44564480")
    val usedMemoryBytes: Long?,

    @Schema(description = "Mémoire maximale allouée à Redis (format lisible). '0B' ou null = pas de limite.", example = "256M")
    val maxMemoryHuman: String?,

    @Schema(description = "Mémoire maximale allouée à Redis en octets. 0 = pas de limite.", example = "268435456")
    val maxMemoryBytes: Long?,

    @Schema(
        description = "Politique d'éviction Redis (maxmemory-policy). Valeur attendue en production : allkeys-lru.",
        example = "allkeys-lru",
    )
    val maxMemoryPolicy: String?,

    @Schema(description = "Ratio de fragmentation mémoire", example = "1.2")
    val memoryFragmentationRatio: String?,

    @Schema(description = "Nombre de clés par préfixe de cache (nom de cache Spring, préfixe avant '::'), trié par nombre décroissant.")
    val keysByCachePrefix: List<CachePrefixKeyCountJson>,
)

data class CachePrefixKeyCountJson(
    @Schema(description = "Préfixe de cache Redis (nom du cache Spring)", example = "consultationCache")
    val prefix: String,

    @Schema(description = "Nombre de clés pour ce préfixe", example = "532")
    val keyCount: Long,
)

fun RedisCacheStats.toJson(): RedisCacheStatsJson = RedisCacheStatsJson(
    totalKeys = totalKeys,
    usedMemoryHuman = usedMemoryHuman,
    usedMemoryBytes = usedMemoryBytes,
    maxMemoryHuman = maxMemoryHuman,
    maxMemoryBytes = maxMemoryBytes,
    maxMemoryPolicy = maxMemoryPolicy,
    memoryFragmentationRatio = memoryFragmentationRatio,
    keysByCachePrefix = keysByCachePrefix.map {
        CachePrefixKeyCountJson(prefix = it.prefix, keyCount = it.keyCount)
    },
)
