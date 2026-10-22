package fr.gouv.agora.infrastructure.consultationResults.repository

import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheRepository
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class ConsultationResultsCacheRepositoryImpl(
    private val clock: Clock,
) : ConsultationResultsCacheRepository {

    companion object {
        private val TTL = Duration.ofHours(1)
    }

    private val logger = LoggerFactory.getLogger(ConsultationResultsCacheRepositoryImpl::class.java)

    private data class CacheEntry(
        val results: ConsultationResults?,
        val cachedAt: Instant,
    )

    private val cache = ConcurrentHashMap<String, CacheEntry>()

    override fun getConsultationResults(consultationId: String): ConsultationResultsCacheResult {
        val entry = cache[consultationId]

        if (entry == null) {
            logger.info("[ConsultationResultsCache] MISS - consultationId=\"{}\"", consultationId)
            return ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized
        }

        val ageSeconds = Duration.between(entry.cachedAt, Instant.now(clock)).seconds
        if (ageSeconds >= TTL.seconds) {
            cache.remove(consultationId)
            logger.info(
                "[ConsultationResultsCache] EXPIRED - consultationId=\"{}\" (age={}s)",
                consultationId, ageSeconds
            )
            return ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized
        }

        return if (entry.results != null) {
            logger.info("[ConsultationResultsCache] HIT - consultationId=\"{}\"", consultationId)
            ConsultationResultsCacheResult.CachedConsultationResults(entry.results)
        } else {
            logger.info("[ConsultationResultsCache] HIT (not found) - consultationId=\"{}\"", consultationId)
            ConsultationResultsCacheResult.ConsultationResultsNotFound
        }
    }

    override fun initConsultationResults(consultationId: String, results: ConsultationResults) {
        cache[consultationId] = CacheEntry(results = results, cachedAt = Instant.now(clock))
        logger.info("[ConsultationResultsCache] WRITE - consultationId=\"{}\"", consultationId)
    }

    override fun initConsultationResultsNotFound(consultationId: String) {
        cache[consultationId] = CacheEntry(results = null, cachedAt = Instant.now(clock))
        logger.info("[ConsultationResultsCache] WRITE (not found) - consultationId=\"{}\"", consultationId)
    }

    override fun evictConsultationResultsCache(consultationId: String) {
        cache.remove(consultationId)
        logger.info("[ConsultationResultsCache] EVICT - consultationId=\"{}\"", consultationId)
    }
}
