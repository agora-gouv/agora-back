package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.infrastructure.userAnsweredConsultation.repository.UserAnsweredConsultationDatabaseRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
@Suppress("unused")
class ConsultationInfoRepositoryImpl(
    private val consultationsDatabaseRepository: ConsultationDatabaseRepository,
    private val strapiRepository: ConsultationStrapiRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val userAnsweredConsultationsDatabaseRepository: UserAnsweredConsultationDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val consultationInfoMapper: ConsultationInfoMapper,
    private val clock: Clock,
    private val cacheManager: CacheManager,
) : ConsultationInfoRepository {
    companion object {
        const val CONSULTATION_CACHE_NAME = "consultationCache"
    }

    override fun getOngoingConsultations(): List<ConsultationPreview> {
        val today = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseOngoingConsultations = consultationsDatabaseRepository.getConsultationOngoingList(today)
            .let { consultationInfoMapper.toConsultationPreview(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseOngoingConsultations
        }

        val strapiOngoingConsultations = strapiRepository.getConsultationsOngoing(today)
            .let { consultationInfoMapper.toConsultationPreview(it, thematiques) }

        return databaseOngoingConsultations + strapiOngoingConsultations
    }

    override fun getFinishedConsultations(): List<ConsultationPreviewFinished> {
        val now = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseConsultations = consultationsDatabaseRepository
            .getConsultationsFinishedPreviewWithUpdateInfo(now)
            .let { consultationInfoMapper.toDomainFinished(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseConsultations
        }
        val consultationsStrapi = strapiRepository.getConsultationsFinished(now)
        val strapiConsultations = consultationsStrapi
            .let { consultationInfoMapper.toDomainFinished(it, thematiques, now) }

        return strapiConsultations + databaseConsultations
    }

    override fun getAnsweredConsultations(userId: String): List<ConsultationPreviewFinished> {
        val userUUID = userId.toUuidOrNull() ?: return emptyList()

        val now = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseAnsweredConsultations = consultationsDatabaseRepository
            .getConsultationsAnsweredPreviewWithUpdateInfo(userUUID)
            .let { consultationInfoMapper.toDomainFinished(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseAnsweredConsultations
        }

        val userAnsweredConsultationsId = userAnsweredConsultationsDatabaseRepository
            .getAnsweredConsultationIds(userUUID).map { it.toString() }

        val strapiAnsweredConsultations = strapiRepository.getConsultationsByIds(userAnsweredConsultationsId)
            .let { consultationInfoMapper.toDomainFinished(it, thematiques, now) }

        return databaseAnsweredConsultations + strapiAnsweredConsultations
    }

    override fun isConsultationExists(consultationId: String): Boolean {
        consultationId.toUuidOrNull()?.let {
            return consultationsDatabaseRepository.existsById(it)
        }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return false
        }

        return strapiRepository.isConsultationExists(consultationId)
    }

    override fun getConsultation(consultationId: String): ConsultationInfo? {
        val cachedConsultationInfo = try {
            getCache()?.get(consultationId, ConsultationInfo::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        if (cachedConsultationInfo != null) return cachedConsultationInfo

        // si l'id est un UUID, c'est forcément un id de la db, les ids Strapi sont des Int
        consultationId.toUuidOrNull()?.let {
            val consultationDto = consultationsDatabaseRepository.getConsultation(it) ?: return null
            val thematiques = thematiqueRepository.getThematiqueList()
            val consultationInfoFromDb = consultationInfoMapper.toConsultationInfo(consultationDto, thematiques)
            getCache()?.put(consultationId, consultationInfoFromDb)
            return consultationInfoFromDb
        }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return null
        }

        val strapiConsultationDTO = strapiRepository.getConsultationById(consultationId)
        val consultationsInfo = consultationInfoMapper.toConsultationInfo(strapiConsultationDTO)
        if (consultationsInfo.isEmpty()) return null
        getCache()?.put(consultationId, consultationsInfo.first())

        return consultationsInfo.first()
    }

    override fun getConsultationsToAggregate(): List<ConsultationPreview> {
        val now = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseConsultationsToAggregate = consultationsDatabaseRepository.getConsultationsEnded14DaysAgo(now)
            .let { consultationInfoMapper.toConsultationPreview(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseConsultationsToAggregate
        }

        val strapiConsultationsToAggregate = strapiRepository.getConsultationsEnded14DaysAgo(now)
            .let { consultationInfoMapper.toConsultationPreview(it, thematiques) }

        return databaseConsultationsToAggregate + strapiConsultationsToAggregate
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_CACHE_NAME)
}
