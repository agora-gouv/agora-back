package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsultationInfoRepositoryImpl(
    private val databaseRepository: ConsultationDatabaseRepository,
    private val consultationInfoMapper: ConsultationInfoMapper,
    private val cacheManager: CacheManager,
) : ConsultationInfoRepository {

    companion object {
        const val CONSULTATION_CACHE_NAME = "consultationCache"
        private const val CONSULTATION_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getOngoingConsultations(): List<ConsultationInfo> {
        return databaseRepository.getConsultationOngoingList().map(consultationInfoMapper::toDomain)
    }

    override fun getFinishedConsultations(): List<ConsultationWithUpdateInfo> {
        return databaseRepository.getConsultationsFinishedPreviewWithUpdateInfo().map(consultationInfoMapper::toDomain)
    }

    override fun getAnsweredConsultations(userId: String): List<ConsultationWithUpdateInfo> {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getConsultationsAnsweredPreviewWithUpdateInfo(userUUID)
                .map(consultationInfoMapper::toDomain)
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
                consultationInfoMapper.toDomain(dto)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun getConsultationsToAggregate(): List<ConsultationInfo> {
        return databaseRepository.getConsultationsToAggregate().map(consultationInfoMapper::toDomain)
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