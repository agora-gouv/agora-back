package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ConsultationUpdateCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val CONSULTATION_UPDATE_CACHE = "consultationUpdateCache"
        private const val CONSULTATION_UPDATE_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"

        private const val ONGOING_UPDATE_PREFIX = "ongoing/"
        private const val FINISHED_UPDATE_PREFIX = "finished/"
    }

    sealed class CacheResult {
        data class CachedConsultationUpdate(val consultationUpdateDTO: ConsultationUpdateDTO) : CacheResult()
        object CachedConsultationUpdateNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getOngoingConsultationUpdate(consultationUUID: UUID): CacheResult {
        return getConsultationUpdate(consultationUUID = consultationUUID, prefix = ONGOING_UPDATE_PREFIX)
    }

    fun getFinishedConsultationUpdate(consultationUUID: UUID): CacheResult {
        return getConsultationUpdate(consultationUUID = consultationUUID, prefix = FINISHED_UPDATE_PREFIX)
    }

    fun insertOngoingConsultationUpdate(
        consultationUUID: UUID,
        consultationUpdatesDTO: ConsultationUpdateDTO,
    ) {
        insertConsultationUpdate(
            consultationUUID = consultationUUID,
            consultationUpdatesDTO = consultationUpdatesDTO,
            prefix = ONGOING_UPDATE_PREFIX,
        )
    }

    fun insertFinishedConsultationUpdate(
        consultationUUID: UUID,
        consultationUpdatesDTO: ConsultationUpdateDTO,
    ) {
        insertConsultationUpdate(
            consultationUUID = consultationUUID,
            consultationUpdatesDTO = consultationUpdatesDTO,
            prefix = FINISHED_UPDATE_PREFIX,
        )
    }

    private fun getConsultationUpdate(consultationUUID: UUID, prefix: String): CacheResult {
        val consultationUpdateDTO = try {
            getCache()?.get(prefix + consultationUUID.toString(), ConsultationUpdateDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }

        return when (consultationUpdateDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            CONSULTATION_UPDATE_NOT_FOUND_ID -> CacheResult.CachedConsultationUpdateNotFound
            else -> CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
        }
    }

    private fun insertConsultationUpdate(
        consultationUUID: UUID,
        consultationUpdatesDTO: ConsultationUpdateDTO,
        prefix: String,
    ) {
        getCache()?.put(prefix + consultationUUID.toString(), consultationUpdatesDTO)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_UPDATE_CACHE)

}