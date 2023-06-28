package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ExplanationCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val EXPLANATION_CACHE_NAME = "explanationCache"
    }

    sealed class CacheResult {
        data class CachedExplanationList(val explanationDTOList: List<ExplanationDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getExplanationList(consultationUpdatesUUID: UUID): CacheResult {
        val explanationDTOList = try {
            getCache()?.get(consultationUpdatesUUID.toString(), List::class.java) as? List<ExplanationDTO>
        } catch (e: IllegalStateException) {
            null
        }

        return when (explanationDTOList) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedExplanationList(explanationDTOList)
        }
    }

    fun insertExplanationList(consultationUpdatesUUID: UUID, explanationDTOList: List<ExplanationDTO>) {
        getCache()?.put(consultationUpdatesUUID.toString(), explanationDTOList)
    }

    private fun getCache() = cacheManager.getCache(EXPLANATION_CACHE_NAME)

}