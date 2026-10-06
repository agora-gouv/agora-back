package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheByIdRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class ConsultationStrapiCacheByIdRepositoryImpl(
    private val clock: Clock,
) : ConsultationStrapiCacheByIdRepository {

    companion object {
        private val TTL = Duration.ofMinutes(5)
    }

    private val logger = LoggerFactory.getLogger(ConsultationStrapiCacheByIdRepositoryImpl::class.java)

    private data class CacheEntry(
        val dto: ConsultationStrapiDTO?,
        val cachedAt: Instant,
    )

    private val cacheById = ConcurrentHashMap<String, CacheEntry>()
    private val cacheByIdWithUnpublished = ConcurrentHashMap<String, CacheEntry>()

    override fun getConsultationById(consultationId: String): ConsultationStrapiDTO? {
        return getFromCache(cacheById, consultationId, "(byId)")
    }

    override fun putConsultationById(consultationId: String, dto: ConsultationStrapiDTO?) {
        putInCache(cacheById, consultationId, dto, "(byId)")
    }

    override fun getConsultationByIdWithUnpublished(consultationId: String): ConsultationStrapiDTO? {
        return getFromCache(cacheByIdWithUnpublished, consultationId, "(withUnpublished)")
    }

    override fun putConsultationByIdWithUnpublished(consultationId: String, dto: ConsultationStrapiDTO?) {
        putInCache(cacheByIdWithUnpublished, consultationId, dto, "(withUnpublished)")
    }

    override fun evictConsultationById(consultationId: String) {
        cacheById.remove(consultationId)
        cacheByIdWithUnpublished.remove(consultationId)
        logger.info("[ConsultationStrapiByIdCache] EVICT - consultationId=\"{}\"", consultationId)
    }

    private fun getFromCache(
        cache: ConcurrentHashMap<String, CacheEntry>,
        consultationId: String,
        variant: String,
    ): ConsultationStrapiDTO? {
        val entry = cache[consultationId]

        if (entry == null) {
            logger.info("[ConsultationStrapiByIdCache] MISS {} - clé=\"{}\"", variant, consultationId)
            return null
        }

        val ageSeconds = Duration.between(entry.cachedAt, Instant.now(clock)).seconds
        if (ageSeconds >= TTL.seconds) {
            cache.remove(consultationId)
            logger.info(
                "[ConsultationStrapiByIdCache] EXPIRED {} - clé=\"{}\" (age={}s)",
                variant, consultationId, ageSeconds
            )
            return null
        }

        return if (entry.dto == null) {
            logger.info(
                "[ConsultationStrapiByIdCache] HIT {} (null sentinel) - clé=\"{}\"",
                variant, consultationId
            )
            null
        } else {
            logger.info(
                "[ConsultationStrapiByIdCache] HIT {} - clé=\"{}\" → slug={}",
                variant, consultationId, entry.dto.slug
            )
            entry.dto
        }
    }

    private fun putInCache(
        cache: ConcurrentHashMap<String, CacheEntry>,
        consultationId: String,
        dto: ConsultationStrapiDTO?,
        variant: String,
    ) {
        cache[consultationId] = CacheEntry(dto = dto, cachedAt = Instant.now(clock))
        logger.info("[ConsultationStrapiByIdCache] WRITE {} - clé=\"{}\"", variant, consultationId)
    }
}
