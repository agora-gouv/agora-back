package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ConsultationUpdatesCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val CONSULTATION_UPDATE_CACHE = "consultationUpdateCache"
        private const val CONSULTATION_UPDATE_NOT_FOUND_ID = "00000000-0000-0000-0000-000000000000"
    }

    sealed class CacheResult {
        data class CachedConsultationUpdate(val consultationUpdateDTO: ConsultationUpdateDTO) : CacheResult()
        object CachedConsultationUpdateNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getConsultationUpdate(consultationUUID: UUID): CacheResult {
        val consultationUpdateDTO = try {
            getCache()?.get(consultationUUID.toString(), ConsultationUpdateDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }

        return when (consultationUpdateDTO?.id?.toString()) {
            null -> CacheResult.CacheNotInitialized
            CONSULTATION_UPDATE_NOT_FOUND_ID -> CacheResult.CachedConsultationUpdateNotFound
            else -> CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
        }
    }

    fun insertConsultationUpdate(consultationUUID: UUID, consultationUpdatesDTO: ConsultationUpdateDTO) {
        getCache()?.put(consultationUUID.toString(), consultationUpdatesDTO)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_UPDATE_CACHE)

}