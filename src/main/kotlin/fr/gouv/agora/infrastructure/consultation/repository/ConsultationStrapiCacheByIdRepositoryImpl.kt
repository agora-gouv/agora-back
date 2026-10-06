package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheByIdRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class ConsultationStrapiCacheByIdRepositoryImpl(
    @Qualifier("shortTermCacheManager")
    private val shortTermCacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationStrapiCacheByIdRepository {

    companion object {
        const val CONSULTATION_BY_ID_CACHE_NAME = "strapiConsultationById"
        const val CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME = "strapiConsultationByIdWithUnpublished"
    }

    private val logger = LoggerFactory.getLogger(ConsultationStrapiCacheByIdRepositoryImpl::class.java)

    override fun getConsultationById(consultationId: String): ConsultationStrapiDTO? {
        return getFromCache(CONSULTATION_BY_ID_CACHE_NAME, consultationId, "(byId)")
    }

    override fun putConsultationById(consultationId: String, dto: ConsultationStrapiDTO?) {
        putInCache(CONSULTATION_BY_ID_CACHE_NAME, consultationId, dto, "(byId)")
    }

    override fun getConsultationByIdWithUnpublished(consultationId: String): ConsultationStrapiDTO? {
        return getFromCache(CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME, consultationId, "(withUnpublished)")
    }

    override fun putConsultationByIdWithUnpublished(consultationId: String, dto: ConsultationStrapiDTO?) {
        putInCache(CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME, consultationId, dto, "(withUnpublished)")
    }

    override fun evictConsultationById(consultationId: String) {
        shortTermCacheManager.getCache(CONSULTATION_BY_ID_CACHE_NAME)?.evict(consultationId)
        shortTermCacheManager.getCache(CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME)?.evict(consultationId)
        logger.info("[ConsultationStrapiByIdCache] EVICT - consultationId=\"{}\"", consultationId)
    }

    private fun getFromCache(cacheName: String, consultationId: String, variant: String): ConsultationStrapiDTO? {
        logger.info("[ConsultationStrapiByIdCache] GET {} - clé=\"{}\"", variant, consultationId)
        return try {
            val cached = shortTermCacheManager.getCache(cacheName)?.get(consultationId, String::class.java)
            when {
                cached == null -> {
                    logger.info("[ConsultationStrapiByIdCache] MISS {} - clé=\"{}\"", variant, consultationId)
                    null
                }
                cached == "null" -> {
                    logger.info("[ConsultationStrapiByIdCache] HIT {} (null) - clé=\"{}\"", variant, consultationId)
                    null
                }
                else -> {
                    val dto = objectMapper.readValue(cached, ConsultationStrapiDTO::class.java)
                    logger.info(
                        "[ConsultationStrapiByIdCache] HIT {} - clé=\"{}\" → slug={}",
                        variant, consultationId, dto.slug
                    )
                    dto
                }
            }
        } catch (e: Exception) {
            logger.warn(
                "[ConsultationStrapiByIdCache] READ ERROR {} - clé=\"{}\" : {}",
                variant, consultationId, e.message
            )
            null
        }
    }

    private fun putInCache(cacheName: String, consultationId: String, dto: ConsultationStrapiDTO?, variant: String) {
        try {
            val serialized = if (dto != null) objectMapper.writeValueAsString(dto) else "null"
            shortTermCacheManager.getCache(cacheName)?.put(consultationId, serialized)
            logger.info("[ConsultationStrapiByIdCache] WRITE {} - clé=\"{}\"", variant, consultationId)
        } catch (e: Exception) {
            logger.warn(
                "[ConsultationStrapiByIdCache] WRITE ERROR {} - clé=\"{}\" : {}",
                variant, consultationId, e.message
            )
        }
    }
}
