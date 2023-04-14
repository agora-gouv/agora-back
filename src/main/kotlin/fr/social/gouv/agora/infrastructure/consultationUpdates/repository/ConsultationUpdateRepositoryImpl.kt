package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.repository.ConsultationUpdateRepositoryImpl.Companion.CONSULTATION_UPDATE_CACHE
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheConfig
import org.springframework.stereotype.Component
import java.util.*

@Component
@CacheConfig(cacheNames = [CONSULTATION_UPDATE_CACHE])
class ConsultationUpdateRepositoryImpl(
    private val databaseRepository: ConsultationUpdateDatabaseRepository,
    private val mapper: ConsultationUpdateMapper,
    private val cacheManager: CacheManager,
) : ConsultationUpdateRepository {

    companion object {
        const val CONSULTATION_UPDATE_CACHE = "consultationUpdateCache"
        private const val CONSULTATION_UPDATE_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    override fun getConsultationUpdate(consultationId: String): ConsultationUpdate? {
        return try {
            val consultationUUID = UUID.fromString(consultationId)
            val cacheResult = getConsultationUpdateFromCache(consultationUUID)

            when (cacheResult) {
                CacheResult.CacheNotInitialized -> getConsultationUpdateFromDatabase(consultationUUID)
                CacheResult.CachedConsultationUpdateNotFound -> null
                is CacheResult.CachedConsultationUpdate -> cacheResult.consultationUpdateDTO
            }?.let { consultationUpdateDTO -> mapper.toDomain(consultationUpdateDTO) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_UPDATE_CACHE)

    private fun getConsultationUpdateFromCache(consultationUUID: UUID): CacheResult {
        val consultationUpdateDTO = getCache()?.get(consultationUUID.toString(), ConsultationUpdateDTO::class.java)

        return when (consultationUpdateDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            CONSULTATION_UPDATE_NOT_FOUND_ID -> CacheResult.CachedConsultationUpdateNotFound
            else -> CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
        }
    }

    private fun getConsultationUpdateFromDatabase(consultationUUID: UUID): ConsultationUpdateDTO? {
        val consultationUpdateDTO = databaseRepository.getLastConsultationUpdate(consultationUUID)
        val putDto = consultationUpdateDTO ?: createConsultationUpdateNotFound()
        getCache()?.put(consultationUUID.toString(), putDto)
        return consultationUpdateDTO
    }

    private fun createConsultationUpdateNotFound() = ConsultationUpdateDTO(
        id = UUID.fromString(CONSULTATION_UPDATE_NOT_FOUND_ID),
        step = 0,
        description = "",
        consultationId = UUID.fromString(CONSULTATION_UPDATE_NOT_FOUND_ID),
    )

}

private sealed class CacheResult {
    data class CachedConsultationUpdate(val consultationUpdateDTO: ConsultationUpdateDTO) : CacheResult()
    object CachedConsultationUpdateNotFound : CacheResult()
    object CacheNotInitialized : CacheResult()
}