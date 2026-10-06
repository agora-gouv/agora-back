package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.userAnsweredConsultation.repository.UserAnsweredConsultationDatabaseRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationInfoRepositoryImpl(
    private val strapiRepository: ConsultationStrapiRepository,
    private val userAnsweredConsultationsDatabaseRepository: UserAnsweredConsultationDatabaseRepository,
    private val consultationInfoMapper: ConsultationInfoMapper,
    private val clock: Clock,
    private val cacheManager: CacheManager,
    private val consultationStrapiCacheRepository: ConsultationStrapiCacheRepository,
) : ConsultationInfoRepository {
    companion object {
        const val CONSULTATION_CACHE_NAME = "consultationCache"
    }

    private val logger = LoggerFactory.getLogger(ConsultationInfoRepositoryImpl::class.java)

    override fun getOngoingConsultations(userTerritoires: List<Territoire>): List<ConsultationPreview> {
        val today = LocalDateTime.now(clock)

        val cached = consultationStrapiCacheRepository.getOngoingConsultations(userTerritoires)
        if (cached != null) {
            return consultationInfoMapper.toConsultationPreviewFromDTOs(cached)
        }

        val result = strapiRepository.getConsultationsOngoing(today, userTerritoires)
        consultationStrapiCacheRepository.putOngoingConsultations(userTerritoires, result.data)
        return consultationInfoMapper.toConsultationPreview(result)
    }

    override fun getOngoingConsultationsWithUnpublished(userTerritoires: List<Territoire>): List<ConsultationPreview> {
        val today = LocalDateTime.now(clock)

        return strapiRepository.getConsultationsOngoingWithUnpublished(today, userTerritoires)
            .let { consultationInfoMapper.toConsultationPreview(it) }
    }

    override fun getFinishedConsultations(userTerritoires: List<Territoire>): List<ConsultationPreviewFinished> {
        val now = LocalDateTime.now(clock)

        val cached = consultationStrapiCacheRepository.getFinishedConsultations(userTerritoires)
        if (cached != null) {
            return consultationInfoMapper.toDomainFinishedFromDTOs(cached, now)
        }

        val result = strapiRepository.getConsultationsFinished(now, userTerritoires)
        consultationStrapiCacheRepository.putFinishedConsultations(userTerritoires, result.data)
        return consultationInfoMapper.toDomainFinished(result, now)
    }

    override fun getFinishedConsultationsWithUnpublished(userTerritoires: List<Territoire>): List<ConsultationPreviewFinished> {
        val now = LocalDateTime.now(clock)

        return strapiRepository.getConsultationsFinishedWithUnpublished(now, userTerritoires)
            .let { consultationInfoMapper.toDomainFinished(it, now) }
    }

    override fun getAnsweredConsultations(userId: String): List<ConsultationPreviewFinished> {
        val userUUID = userId.toUuidOrNull() ?: return emptyList()
        val now = LocalDateTime.now(clock)

        val strapiAnsweredConsultationIds = userAnsweredConsultationsDatabaseRepository
            .getAnsweredConsultationIds(userUUID)

        val strapiAnsweredConsultations = strapiRepository.getConsultationsByIds(strapiAnsweredConsultationIds)
            .let { consultationInfoMapper.toDomainFinished(it, now) }

        return strapiAnsweredConsultations
    }

    override fun isConsultationExists(consultationId: String): Boolean {
        return strapiRepository.isConsultationExists(consultationId)
    }

    override fun getConsultationByIdOrSlug(consultationIdOrSlug: String): ConsultationInfo? {
        logger.info("[ConsultationInfoCache] GET - clé=\"{}\"", consultationIdOrSlug)
        val cachedConsultationInfo = try {
            getCache()?.get(consultationIdOrSlug, ConsultationInfo::class.java)
        } catch (e: IllegalStateException) {
            logger.info("[ConsultationInfoCache] READ ERROR - clé=\"{}\" : {}", consultationIdOrSlug, e.message)
            null
        }
        if (cachedConsultationInfo != null) {
            logger.info("[ConsultationInfoCache] HIT - clé=\"{}\" → id={}, slug={}", consultationIdOrSlug, cachedConsultationInfo.id, cachedConsultationInfo.slug)
            return cachedConsultationInfo
        }
        logger.info("[ConsultationInfoCache] MISS - clé=\"{}\"", consultationIdOrSlug)

        val bySlug = strapiRepository.getConsultationBySlug(consultationIdOrSlug)
        logger.info("[ConsultationInfoCache] STRAPI SLUG - clé=\"{}\" → trouvé={}", consultationIdOrSlug, bySlug != null)
        val consultationFromStrapi = bySlug
            ?: run {
                val byId = strapiRepository.getConsultationById(consultationIdOrSlug)
                logger.info("[ConsultationInfoCache] STRAPI ID - clé=\"{}\" → trouvé={}", consultationIdOrSlug, byId != null)
                byId
            }
            ?: return null
        val consultationsInfo = consultationInfoMapper.toConsultationInfo(consultationFromStrapi)
        putInCacheUnderBothKeys(consultationIdOrSlug, consultationsInfo)

        return consultationsInfo
    }

    override fun getConsultationByIdOrSlugWithUnpublished(consultationIdOrSlug: String): ConsultationInfo? {
        logger.info("[ConsultationInfoCache] GET (unpublished) - clé=\"{}\"", consultationIdOrSlug)
        val cachedConsultationInfo = try {
            getCache()?.get(consultationIdOrSlug, ConsultationInfo::class.java)
        } catch (e: IllegalStateException) {
            logger.info("[ConsultationInfoCache] READ ERROR (unpublished) - clé=\"{}\" : {}", consultationIdOrSlug, e.message)
            null
        }
        if (cachedConsultationInfo != null) {
            logger.info("[ConsultationInfoCache] HIT (unpublished) - clé=\"{}\" → id={}, slug={}", consultationIdOrSlug, cachedConsultationInfo.id, cachedConsultationInfo.slug)
            return cachedConsultationInfo
        }
        logger.info("[ConsultationInfoCache] MISS (unpublished) - clé=\"{}\"", consultationIdOrSlug)

        val bySlug = strapiRepository.getConsultationBySlugWithUnpublished(consultationIdOrSlug)
        logger.info("[ConsultationInfoCache] STRAPI SLUG (unpublished) - clé=\"{}\" → trouvé={}", consultationIdOrSlug, bySlug != null)
        val consultationFromStrapi = bySlug
            ?: run {
                val byId = strapiRepository.getConsultationByIdWithUnpublished(consultationIdOrSlug)
                logger.info("[ConsultationInfoCache] STRAPI ID (unpublished) - clé=\"{}\" → trouvé={}", consultationIdOrSlug, byId != null)
                byId
            }
            ?: return null
        val consultationsInfo = consultationInfoMapper.toConsultationInfo(consultationFromStrapi)
        putInCacheUnderBothKeys(consultationIdOrSlug, consultationsInfo)

        return consultationsInfo
    }

    override fun getConsultation(consultationId: String): ConsultationInfo? {
        logger.info("[ConsultationInfoCache] GET (byId) - clé=\"{}\"", consultationId)
        val cachedConsultationInfo = try {
            getCache()?.get(consultationId, ConsultationInfo::class.java)
        } catch (e: IllegalStateException) {
            logger.info("[ConsultationInfoCache] READ ERROR (byId) - clé=\"{}\" : {}", consultationId, e.message)
            null
        }
        if (cachedConsultationInfo != null) {
            logger.info("[ConsultationInfoCache] HIT (byId) - clé=\"{}\" → id={}, slug={}", consultationId, cachedConsultationInfo.id, cachedConsultationInfo.slug)
            return cachedConsultationInfo
        }
        logger.info("[ConsultationInfoCache] MISS (byId) - clé=\"{}\"", consultationId)

        val strapiConsultationDTO = strapiRepository.getConsultationById(consultationId) ?: return null
        val consultationsInfo = consultationInfoMapper.toConsultationInfo(strapiConsultationDTO)
        getCache()?.put(consultationId, consultationsInfo)
        logger.info("[ConsultationInfoCache] WRITE (byId) - clé=\"{}\"", consultationId)

        return consultationsInfo
    }

    override fun getConsultationsToAggregate(): List<ConsultationPreview> {
        val now = LocalDateTime.now(clock)

        val strapiConsultationsToAggregate = strapiRepository.getConsultationsEnded14DaysAgo(now)
            .let { consultationInfoMapper.toConsultationPreview(it) }

        return strapiConsultationsToAggregate
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_CACHE_NAME)

    private fun putInCacheUnderBothKeys(consultationIdOrSlug: String, consultationInfo: ConsultationInfo) {
        val cache = getCache() ?: return
        val keys = mutableListOf(consultationIdOrSlug)
        cache.put(consultationIdOrSlug, consultationInfo)
        if (consultationInfo.id != consultationIdOrSlug) {
            cache.put(consultationInfo.id, consultationInfo)
            keys.add(consultationInfo.id)
        }
        if (consultationInfo.slug != consultationIdOrSlug && consultationInfo.slug != consultationInfo.id) {
            cache.put(consultationInfo.slug, consultationInfo)
            keys.add(consultationInfo.slug)
        }
        logger.info("[ConsultationInfoCache] WRITE - clés={}", keys)
    }
}
