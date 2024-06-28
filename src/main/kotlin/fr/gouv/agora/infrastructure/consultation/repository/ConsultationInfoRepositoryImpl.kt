package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
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
    private val databaseRepository: ConsultationDatabaseRepository,
    private val strapiRepository: ConsultationStrapiRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val clock: Clock,
    private val consultationInfoMapper: ConsultationInfoMapper,
    private val cacheManager: CacheManager,
) : ConsultationInfoRepository {

    companion object {
        const val CONSULTATION_CACHE_NAME = "consultationCache"
        private const val CONSULTATION_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getOngoingConsultations(): List<ConsultationPreviewOngoing> {
        val today = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseOngoingConsultations = databaseRepository.getConsultationOngoingList(today)
            .let { consultationInfoMapper.toDomainOngoing(it, thematiques) }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseOngoingConsultations
        }

        val strapiOngoingConsultations = strapiRepository.getConsultationsOngoing(today)
            .let { consultationInfoMapper.toDomainOngoing(it, thematiques) }

        return databaseOngoingConsultations + strapiOngoingConsultations
    }

    override fun getFinishedConsultations(): List<ConsultationPreviewFinished> {
        val today = LocalDateTime.now(clock)
        val thematiques = thematiqueRepository.getThematiqueList()
        val databaseConsultations = databaseRepository
            .getConsultationsFinishedPreviewWithUpdateInfo(today)
            .let { consultationInfoMapper.toDomainFinished(it, thematiques) }


        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return databaseConsultations
        }

        val updateDate = LocalDateTime.now(clock) // TODO : à changer
        val strapiConsultations = strapiRepository.getConsultationsFinished(today)
            .let { consultationInfoMapper.toDomainFinished(it, thematiques, updateDate) }

        return strapiConsultations + databaseConsultations

        // TODO 34 : récupérer les consultation terminées via Strapi
        // TODO 34 : consultations endDate < today AND l'update la plus récente avec today >= update_date

    }

    override fun getAnsweredConsultations(userId: String): List<ConsultationWithUpdateInfo> {
        // TODO 33 : récupérer les consultations répondues par l'utilisateur avec
        // TODO 33 : les informations update les + récentes mais avec un update date < à la date actuelle
        // TODO 33 : via Strapi
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getConsultationsAnsweredPreviewWithUpdateInfo(userUUID)
                .map(consultationInfoMapper::toDomainOngoing)
        } ?: emptyList()
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

    override fun getConsultationsToAggregate(): List<ConsultationInfo> {
        return databaseRepository.getConsultationsToAggregate().map(consultationInfoMapper::toDomainOngoing)
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
        val consultationDto = databaseRepository.getConsultation(uuid)
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
