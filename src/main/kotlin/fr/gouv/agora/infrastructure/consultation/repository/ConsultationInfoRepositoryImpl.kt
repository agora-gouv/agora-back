package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.userAnsweredConsultation.repository.UserAnsweredConsultationDatabaseRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
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
) : ConsultationInfoRepository {
    companion object {
        const val CONSULTATION_CACHE_NAME = "consultationCache"
    }

    override fun getOngoingConsultationsWithUnpublished(userTerritoires: List<Territoire>): List<ConsultationPreview> {
        val today = LocalDateTime.now(clock)

        val strapiOngoingConsultations = strapiRepository.getConsultationsOngoingWithUnpublished(today, userTerritoires)
            .let { consultationInfoMapper.toConsultationPreview(it) }

        return strapiOngoingConsultations
    }

    override fun getFinishedConsultationsWithUnpublished(userTerritoires: List<Territoire>): List<ConsultationPreviewFinished> {
        val now = LocalDateTime.now(clock)

        val consultationsStrapi = strapiRepository.getConsultationsFinishedWithUnpublished(now, userTerritoires)
        val strapiConsultations = consultationsStrapi
            .let { consultationInfoMapper.toDomainFinished(it, now) }

        return strapiConsultations
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
        return getConsultationByIdOrSlugWithUnpublished(consultationIdOrSlug)
            ?.takeIf { it.isPublished }
    }

    override fun getConsultationByIdOrSlugWithUnpublished(consultationIdOrSlug: String): ConsultationInfo? {
        val cachedConsultationInfo = try {
            getCache()?.get(consultationIdOrSlug, ConsultationInfo::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        if (cachedConsultationInfo != null) return cachedConsultationInfo

        val consultationFromStrapi = strapiRepository.getConsultationBySlugWithUnpublished(consultationIdOrSlug)
            ?: strapiRepository.getConsultationByIdWithUnpublished(consultationIdOrSlug)
            ?: return null
        val consultationsInfo = consultationInfoMapper.toConsultationInfo(consultationFromStrapi)
        getCache()?.put(consultationIdOrSlug, consultationsInfo)

        return consultationsInfo
    }

    override fun getConsultation(consultationId: String): ConsultationInfo? {
        val cachedConsultationInfo = try {
            getCache()?.get(consultationId, ConsultationInfo::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        if (cachedConsultationInfo != null) return cachedConsultationInfo

        val strapiConsultationDTO = strapiRepository.getConsultationById(consultationId) ?: return null
        val consultationsInfo = consultationInfoMapper.toConsultationInfo(strapiConsultationDTO)
        getCache()?.put(consultationId, consultationsInfo)

        return consultationsInfo
    }

    override fun getConsultationsToAggregate(): List<ConsultationPreview> {
        val now = LocalDateTime.now(clock)

        val strapiConsultationsToAggregate = strapiRepository.getConsultationsEnded14DaysAgo(now)
            .let { consultationInfoMapper.toConsultationPreview(it) }

        return strapiConsultationsToAggregate
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_CACHE_NAME)
}
