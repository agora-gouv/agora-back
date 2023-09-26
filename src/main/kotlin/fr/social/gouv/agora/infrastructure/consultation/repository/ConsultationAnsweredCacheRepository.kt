package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ConsultationAnsweredCacheRepository(private val cacheManager: CacheManager) {
    companion object {
        private const val CONSULTATION_LIST_CACHE_NAME = "consultationAnsweredCache"
    }

    sealed class CacheResult {
        data class CachedConsultationAnsweredList(val consultationAnsweredListDTO: List<ConsultationDTO>) :
            CacheResult()

        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getConsultationAnsweredList(userId: UUID): CacheResult {
        return CacheResult.CacheNotInitialized
//        val consultationAnsweredList = try {
//            getCache()?.get(userId.toString(), List::class.java) as? List<ConsultationDTO>
//        } catch (e: IllegalStateException) {
//            null
//        }
//        return when (consultationAnsweredList) {
//            null -> CacheResult.CacheNotInitialized
//            else -> CacheResult.CachedConsultationAnsweredList(consultationAnsweredList)
//        }
    }

    fun insertConsultationAnsweredList(userId: UUID, consultationAnsweredListDTO: List<ConsultationDTO>?) {
        getCache()?.put(
            userId.toString(),
            consultationAnsweredListDTO ?: emptyList<ConsultationDTO>(),
        )
    }

    fun deleteConsultationAnsweredList(userId: UUID) {
        getCache()?.evict(userId.toString())
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_LIST_CACHE_NAME)
}