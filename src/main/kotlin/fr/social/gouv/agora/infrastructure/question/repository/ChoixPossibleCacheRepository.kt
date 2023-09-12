package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ChoixPossibleCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        const val CHOIX_POSSIBLE_CACHE_NAME = "choixPossibleCache"
    }

    sealed class CacheResult {
        data class CachedChoixPossibleList(val choixPossibleDTOList: List<ChoixPossibleDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getChoixPossibleList(questionId: UUID): CacheResult {
        val choixPossibleDTOList = try {
            getCache()?.get(questionId.toString(), List::class.java) as? List<ChoixPossibleDTO>
        } catch (e: IllegalStateException) {
            null
        }

        return when (choixPossibleDTOList) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedChoixPossibleList(choixPossibleDTOList)
        }
    }

    fun insertChoixPossibleList(questionId: UUID, choixPossibleDTOList: List<ChoixPossibleDTO>) {
        getCache()?.put(questionId.toString(), choixPossibleDTOList)
    }

    private fun getCache() = cacheManager.getCache(CHOIX_POSSIBLE_CACHE_NAME)

}