package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ReponseConsultationCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val REPONSE_CONSULTATION_CACHE_NAME = "reponseConsultationCache"
    }

    sealed class CacheResult {
        data class CacheReponseConsultation(val reponseConsultationList: List<ReponseConsultationDTO>) : CacheResult()
        object CacheReponseConsultationNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getReponseConsultationList(consultationId: UUID): CacheResult {
        val reponseConsultationList = try {
            getCache()?.get(consultationId.toString(), List::class.java) as? List<ReponseConsultationDTO>
        } catch (e: IllegalStateException) {
            null
        }

        return when (reponseConsultationList) {
            null -> CacheResult.CacheNotInitialized
            emptyList<ReponseConsultationDTO>() -> CacheResult.CacheReponseConsultationNotFound
            else -> CacheResult.CacheReponseConsultation(reponseConsultationList)
        }
    }

    fun insertReponseConsultationList(consultationId: UUID, reponseConsultationList: List<ReponseConsultationDTO>) {
        val cachedReponseConsultationList = when (val cacheResult = getReponseConsultationList(consultationId)) {
            CacheResult.CacheNotInitialized -> emptyList()
            CacheResult.CacheReponseConsultationNotFound -> emptyList()
            is CacheResult.CacheReponseConsultation -> cacheResult.reponseConsultationList
        }

        getCache()?.put(consultationId.toString(), cachedReponseConsultationList.plus(reponseConsultationList))
    }

    private fun getCache() = cacheManager.getCache(REPONSE_CONSULTATION_CACHE_NAME)

}