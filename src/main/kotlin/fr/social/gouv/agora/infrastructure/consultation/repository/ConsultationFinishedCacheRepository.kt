package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ConsultationFinishedCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val CONSULTATION_FINISHED_CACHE_NAME = "consultationFinishedCache"
        private const val CONSULTATION_FINISHED_CACHE_KEY = "consultationFinishedCacheKey"
    }

    sealed class CacheResult {
        data class CachedConsultationFinishedList(val consultationFinishedListDTO: List<ConsultationDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getConsultationFinishedList(): CacheResult {
        return CacheResult.CacheNotInitialized
//        val consultationFinishedList = try {
//            getCache()?.get(CONSULTATION_FINISHED_CACHE_KEY, List::class.java) as? List<ConsultationDTO>
//        } catch (e: IllegalStateException) {
//            null
//        }
//        return when (consultationFinishedList) {
//            null -> CacheResult.CacheNotInitialized
//            else -> CacheResult.CachedConsultationFinishedList(consultationFinishedList)
//        }
    }

    fun insertConsultationFinishedList(consultationOngoingListDTO: List<ConsultationDTO>?) {
        getCache()?.put(
            CONSULTATION_FINISHED_CACHE_KEY,
            consultationOngoingListDTO?: emptyList<ConsultationDTO>(),
        )
    }

    fun clearCache() {
        getCache()?.evict(CONSULTATION_FINISHED_CACHE_KEY)
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_FINISHED_CACHE_NAME)

}