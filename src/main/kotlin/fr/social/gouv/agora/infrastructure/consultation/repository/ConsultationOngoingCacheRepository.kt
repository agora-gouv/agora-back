package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ConsultationOngoingCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val ONGOING_CACHE_KEY = "consultationOngoingCache"
        private const val CONSULTATION_ONGOING_CACHE_NAME = "consultationListCache"
    }

    sealed class CacheResult {
        data class CachedConsultationOngoingList(val consultationOngoingListDTO: List<ConsultationDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getConsultationOngoingList(): CacheResult {
        val consultationOngoingList = try {
            getCache()?.get(ONGOING_CACHE_KEY, List::class.java) as? List<ConsultationDTO>
        } catch (e: IllegalStateException) {
            null
        }
        return when (consultationOngoingList) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedConsultationOngoingList(consultationOngoingList)
        }
    }

    fun insertConsultationOngoingList(consultationOngoingListDTO: List<ConsultationDTO>?) {
        getCache()?.put(
            ONGOING_CACHE_KEY,
            consultationOngoingListDTO?: emptyList<ConsultationDTO>(),
        )
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_ONGOING_CACHE_NAME)
}