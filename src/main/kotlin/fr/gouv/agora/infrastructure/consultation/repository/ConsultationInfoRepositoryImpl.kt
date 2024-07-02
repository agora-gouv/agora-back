package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
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
import java.util.Date
import java.util.UUID

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

    // TODO : bientot on aura aussi des consultations sans UUID, à gérer !!

    companion object {
        const val CONSULTATION_CACHE_NAME = "consultationCache"
        private const val CONSULTATION_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getOngoingConsultations(): List<ConsultationPreviewOngoing> {
        val today = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseOngoingConsultations = consultationsDatabaseRepository.getConsultationOngoingList(today)
            .let { consultationInfoMapper.toDomainOngoing(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseOngoingConsultations
        }

        val strapiOngoingConsultations = strapiRepository.getConsultationsOngoing(today)
            .let { consultationInfoMapper.toDomainOngoing(it, thematiques) }

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
        val consultationUUID = consultationId.toUuidOrNull()

        val existsInDatabase = if (consultationUUID != null) {
            consultationsDatabaseRepository.getConsultation(consultationUUID) != null
        } else false

        val existsInStrapi = if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            strapiRepository.getConsultationsById(consultationId).meta.pagination.total == 1
        } else false

        return existsInStrapi || existsInDatabase
    }

    override fun getConsultation(consultationId: String): ConsultationInfo? {
        return try {
            val uuid = UUID.fromString(consultationId)
            val cacheResult = getConsultationFromCache(uuid)
            when (cacheResult) {
                CacheResult.CacheNotInitialized -> getConsultationFromDatabase(uuid)
                CacheResult.CachedConsultationNotFound -> null
                is CacheResult.CachedConsultation -> cacheResult.consultationDTO
            }?.let { dto ->
                consultationInfoMapper.toDomainOngoing(dto)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun getConsultationsToAggregate(): List<ConsultationPreviewOngoing> {
        val now = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseConsultationsToAggregate = consultationsDatabaseRepository.getConsultationsEnded14DaysAgo(now)
            .let { consultationInfoMapper.toDomainOngoing(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseConsultationsToAggregate
        }

        val strapiConsultationsToAggregate = strapiRepository.getConsultationsEnded14DaysAgo(now)
            .let { consultationInfoMapper.toDomainOngoing(it, thematiques) }

        return databaseConsultationsToAggregate + strapiConsultationsToAggregate
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_CACHE_NAME)

    private fun getConsultationFromCache(uuid: UUID): CacheResult {
        val cachedDto = try {
            getCache()?.get(uuid.toString(), ConsultationDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
        return when (cachedDto?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            CONSULTATION_NOT_FOUND_ID -> CacheResult.CachedConsultationNotFound
            else -> CacheResult.CachedConsultation(cachedDto)
        }
    }

    private fun getConsultationFromDatabase(uuid: UUID): ConsultationDTO? {
        val consultationDto = consultationsDatabaseRepository.getConsultation(uuid)
        getCache()?.put(uuid.toString(), consultationDto ?: createConsultationNotFound())
        return consultationDto
    }

    private fun createConsultationNotFound() = ConsultationDTO(
        id = UUID.fromString(CONSULTATION_NOT_FOUND_ID),
        title = "",
        startDate = Date(0),
        endDate = Date(0),
        coverUrl = "",
        detailsCoverUrl = "",
        questionCountNumber = 0,
        questionCount = "",
        estimatedTime = "",
        participantCountGoal = 0,
        description = "",
        tipsDescription = "",
        thematiqueId = UUID.fromString(CONSULTATION_NOT_FOUND_ID),
    )
}

private sealed class CacheResult {
    data class CachedConsultation(val consultationDTO: ConsultationDTO) : CacheResult()
    object CachedConsultationNotFound : CacheResult()
    object CacheNotInitialized : CacheResult()
}
