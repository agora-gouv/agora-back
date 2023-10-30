package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ConsultationOngoingCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val CONSULTATION_ONGOING_CACHE_KEY = "consultationOngoingCacheKey"
        private const val CONSULTATION_LIST_CACHE_NAME = "consultationOngoingCache"
    }

    sealed class CacheResult {
        data class CachedConsultationOngoingList(val consultationOngoingListDTO: List<ConsultationDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getConsultationOngoingList(): CacheResult {
        val consultationOngoingList = try {
            getCache()?.get(CONSULTATION_ONGOING_CACHE_KEY, List::class.java) as? List<ConsultationDTO>
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
            CONSULTATION_ONGOING_CACHE_KEY,
            consultationOngoingListDTO?: emptyList<ConsultationDTO>(),
        )
    }

    fun clearCache() {
        getCache()?.evict(CONSULTATION_ONGOING_CACHE_KEY)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_LIST_CACHE_NAME)
}