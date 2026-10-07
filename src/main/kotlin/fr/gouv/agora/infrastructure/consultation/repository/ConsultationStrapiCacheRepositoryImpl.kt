package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class ConsultationStrapiCacheRepositoryImpl(
    private val clock: Clock,
) : ConsultationStrapiCacheRepository {

    companion object {
        private val TTL = Duration.ofMinutes(5)
    }

    private val logger = LoggerFactory.getLogger(ConsultationStrapiCacheRepositoryImpl::class.java)

    private data class CacheEntry(
        val data: List<ConsultationStrapiDTO>,
        val cachedAt: Instant,
    )

    private val ongoingCache = ConcurrentHashMap<String, CacheEntry>()
    private val finishedCache = ConcurrentHashMap<String, CacheEntry>()

    override fun getOngoingConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>? {
        return getFromCache(ongoingCache, toTerritoryKey(territories), "ongoing")
    }

    override fun putOngoingConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>) {
        putInCache(ongoingCache, toTerritoryKey(territories), data, "ongoing")
    }

    override fun evictOngoingConsultations() {
        ongoingCache.clear()
        logger.info("[ConsultationStrapiCache] EVICT - ongoing")
    }

    override fun getFinishedConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>? {
        return getFromCache(finishedCache, toTerritoryKey(territories), "finished")
    }

    override fun putFinishedConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>) {
        putInCache(finishedCache, toTerritoryKey(territories), data, "finished")
    }

    override fun evictFinishedConsultations() {
        finishedCache.clear()
        logger.info("[ConsultationStrapiCache] EVICT - finished")
    }

    private fun toTerritoryKey(territories: List<Territoire>): String {
        return territories.map { it.value }.sorted().joinToString(",").ifEmpty { "all" }
    }

    private fun getFromCache(
        cache: ConcurrentHashMap<String, CacheEntry>,
        cacheKey: String,
        variant: String,
    ): List<ConsultationStrapiDTO>? {
        val entry = cache[cacheKey] ?: run {
            logger.info("[ConsultationStrapiCache] MISS {} - clé=\"{}\"", variant, cacheKey)
            return null
        }

        val ageSeconds = Duration.between(entry.cachedAt, Instant.now(clock)).seconds
        if (ageSeconds >= TTL.seconds) {
            cache.remove(cacheKey)
            logger.info(
                "[ConsultationStrapiCache] EXPIRED {} - clé=\"{}\" (age={}s)",
                variant, cacheKey, ageSeconds
            )
            return null
        }

        logger.info(
            "[ConsultationStrapiCache] HIT {} - clé=\"{}\" → {} consultations",
            variant, cacheKey, entry.data.size
        )
        return entry.data
    }

    private fun putInCache(
        cache: ConcurrentHashMap<String, CacheEntry>,
        cacheKey: String,
        data: List<ConsultationStrapiDTO>,
        variant: String,
    ) {
        cache[cacheKey] = CacheEntry(data = data, cachedAt = Instant.now(clock))
        logger.info(
            "[ConsultationStrapiCache] WRITE {} - clé=\"{}\" → {} consultations",
            variant, cacheKey, data.size
        )
    }
}
